package io.github.edadma.compositor

import io.github.edadma.compositor
import io.github.edadma.libcairo.{
  Context,
  FontSlant,
  FontWeight,
  Format,
  Surface,
  TextExtents,
  pdfSurfaceCreate,
  imageSurfaceCreate,
}

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

abstract class Compositor private[compositor]:
  protected[compositor] val surface: Surface
  protected[compositor] val ctx: Context
  val pageWidth: Int
  val pageHeight: Int

  protected val boxes = new ArrayBuffer[Box]
  protected[compositor] var currentFont: Font = null
  protected var currentColor: Color = new Color(0, 0, 0)
  protected var page = new VBox

  font("sans", FontSlant.NORMAL, FontWeight.NORMAL, 10)

  def add(box: Box): Unit = page add box

  def addWord(text: String): Unit = addBox(textBox(text))

  def addText(text: String): Unit =
    val words = text.split(' ').filterNot(_ == "")

    words foreach addWord

  def addBox(box: Box): Unit =
    if boxes.nonEmpty then
      boxes.last match
        case b: TextBox =>
          if b.text.nonEmpty then
            boxes += new HSpaceBox(
              0,
              if ".!?:" contains b.text.last then b.font.space * 1.5 else b.font.space,
            ) // todo: use font info for spaces
        case _ =>
    else boxes += new RigidBox(width = 36)

    boxes += box

  def font(f: Font): Unit =
    if currentFont ne f then
      ctx.selectFontFace(f.family, f.slant, f.weight)
      ctx.setFontSize(f.size)
      currentFont = f

  def font(family: String, slant: FontSlant, weight: FontWeight, size: Double): Font =
    ctx.selectFontFace(family, slant, weight)
    ctx.setFontSize(size)

    val TextExtents(_, _, _, _, _Width, _) = ctx.textExtents("_")
    val TextExtents(_, _, _, _, _sWithSpaceWidth, _) = ctx.textExtents("_ _")
    val extents = ctx.fontExtents

    currentFont = new Font(family, slant, weight, size, extents, _sWithSpaceWidth - 2 * _Width)
    currentFont

  def line(lineWidth: Double): Unit =
    val hbox = new HBox

    boxes foreach hbox.add
    boxes.clear()
    hbox.set(lineWidth)
    page add hbox

  def paragraph(lineWidth: Double): Unit =
    while boxes.nonEmpty do
      val hbox = new HBox

      @tailrec
      def line(): Unit =
        if boxes.nonEmpty then
          if hbox.width + boxes.head.width <= lineWidth then
            hbox add boxes.remove(0)
            line()
          else
            boxes.head match
              case b: TextBox =>
                b.text indexOf '-' match
                  case -1 =>
                    Hyphenation(b.text) match
                      case None =>
                      case Some(hyphenation) =>
                        var lastBefore: TextBox = null
                        var lastAfter: String = null

                        @tailrec
                        def longest(): Unit =
                          if hyphenation.hasNext then
                            val (before, after) = hyphenation.next
                            val beforeHyphen = b.newTextBox(before)

                            if hbox.width + beforeHyphen.width <= lineWidth then
                              lastBefore = beforeHyphen
                              lastAfter = after
                              longest()

                        longest()

                        if lastBefore ne null then
                          hbox add lastBefore
                          boxes.remove(0)
                          boxes.insert(0, b.newTextBox(lastAfter))
                    end match
                  case idx =>
                    val beforeHyphen = b.newTextBox(b.text.substring(0, idx + 1))

                    if hbox.width + beforeHyphen.width <= lineWidth then
                      hbox add beforeHyphen
                      boxes.remove(0)
                      boxes.insert(0, b.newTextBox(b.text.substring(idx + 1)))
              case _ =>

      line()

      if hbox.boxes.last.isSpace then hbox.boxes.remove(hbox.boxes.length - 1)
      if boxes.nonEmpty && boxes.head.isSpace then boxes.remove(0)
      if boxes.nonEmpty then hbox.set(lineWidth)

      page add hbox
    end while
  end paragraph

  def bold(): Unit = font(currentFont.family, currentFont.slant, FontWeight.BOLD, currentFont.size)

  def normal(): Unit = font(currentFont.family, currentFont.slant, FontWeight.NORMAL, currentFont.size)

  def size(points: Double): Unit = font(currentFont.family, currentFont.slant, currentFont.weight, points)

  def color(r: Double, g: Double, b: Double, a: Double = 1): Unit =
    currentColor = new Color(r, g, b, a)
    ctx.setSourceRGBA(r, g, b, a)

  def sup(s: String): Box =
    val f = currentFont

    bold()

    val shift = -currentFont.size * .3333

    size(currentFont.size * 0.583)

    val res = new ShiftBox(textBox(s), shift)

    font(f)
    res

  def textBox(s: String): TextBox = new TextBox(this, s, currentFont, currentColor)

//  def charBox(text: String): CharBox =
//    val extents = ctx textExtents text
//
//    new CharBox(text, currentFont, currentColor):
//      val height: Double = extents.height
//      val descent: Double = 0
//      val width: Double = extents.width

  def draw(height: Int): Unit

  def destroy(): Unit =
    ctx.destroy()
    surface.destroy()
end Compositor

class PDFCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    val pageWidth: Int,
    val pageHeight: Int,
) extends Compositor:
  def draw(height: Int): Unit =
    page.set(height)
    page.draw(this, 0, 0)
    ctx.showPage()

class PNGCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    path: String,
    val pageWidth: Int,
    val pageHeight: Int,
) extends Compositor:
  def draw(height: Int): Unit =
    page.set(height)
    page.draw(this, 0, 0)
    surface.writeToPNG(path)

object Compositor:
  def pdf(path: String, width: Int, height: Int): Compositor =
    val surface = pdfSurfaceCreate(path, width, height)
    val context = surface.create

    new PDFCompositor(surface, context, width, height)

  def png(path: String, width: Int, height: Int, ppi: Double): Compositor =
    val pixelsPerPoint = ppi / 72
    val surface = imageSurfaceCreate(Format.ARGB32, (width * pixelsPerPoint).toInt, (height * pixelsPerPoint).toInt)
    val context = surface.create

    context.scale(pixelsPerPoint, pixelsPerPoint)
    new PNGCompositor(surface, context, path, width, height)
end Compositor
