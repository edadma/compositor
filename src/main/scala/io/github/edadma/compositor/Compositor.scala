package io.github.edadma.compositor

import io.github.edadma.compositor
import io.github.edadma.libcairo.{
  Context,
  FontSlant,
  FontWeight,
  Format,
  Surface,
  TextExtents,
  imageSurfaceCreate,
  pdfSurfaceCreate,
}

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

abstract class Compositor private[compositor]:
  protected[compositor] val surface: Surface
  protected[compositor] val ctx: Context
  val pageWidth: Double
  val pageHeight: Double
  val pageFactory: (Compositor, Double, Double) => PageBox

  protected val boxes = new ArrayBuffer[Box]
  protected[compositor] var currentFont: Font = null
  protected var currentColor: Color = new Color(0, 0, 0)
  protected var page: PageBox = pageFactory(this, pageWidth, pageHeight)

  font("sans", FontSlant.NORMAL, FontWeight.NORMAL, 10)

  def setPage(box: PageBox): Unit = page = box

  def add(box: Box): Unit = page add box

  val `LEFT SINGLE QUOTATION MARK` = "\u2018"
  val `RIGHT SINGLE QUOTATION MARK` = "\u2019"
  val `LEFT DOUBLE QUOTATION MARK` = "\u201C"
  val `RIGHT DOUBLE QUOTATION MARK` = "\u201D"

  def addWord(text: String): Unit =
    val text1 =
      text
        .replace("``", `LEFT DOUBLE QUOTATION MARK`)
        .replace("''", `RIGHT DOUBLE QUOTATION MARK`)
        .replace("`", `LEFT SINGLE QUOTATION MARK`)
        .replace("'", `RIGHT SINGLE QUOTATION MARK`)
    addBox(charBox(text1))

  def addText(text: String): Unit =
    val words = text.split(' ').filterNot(_ == "")

    words foreach addWord

  def addBox(box: Box): Unit =
    if boxes.nonEmpty then
      boxes.last match
        case b: CharBox =>
          if b.text.nonEmpty then
            val space =
              if b.text.last == '.' && Abbreviation(b.text.dropRight(1)) then b.font.space
              else if ".!?:;" contains b.text.last then b.font.space * 1.5
              else b.font.space

            boxes += new HSpaceBox(
              0,
              space,
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

  def line(): Unit =
    val hbox = new HBox

    boxes foreach hbox.add
    boxes.clear()
    hbox.set(page.lineWidth)
    page add hbox

  def paragraph(): Unit =
    while boxes.nonEmpty do
      val hbox = new HBox

      @tailrec
      def line(): Unit =
        if boxes.nonEmpty then
          if hbox.width + boxes.head.width <= page.lineWidth then
            hbox add boxes.remove(0)
            line()
          else
            boxes.head match
              case b: CharBox =>
                b.text indexOf '-' match
                  case -1 =>
                    Hyphenation(b.text) match
                      case None =>
                      case Some(hyphenation) =>
                        var lastBefore: CharBox = null
                        var lastAfter: String = null

                        @tailrec
                        def longest(): Unit =
                          if hyphenation.hasNext then
                            val (before, after) = hyphenation.next
                            val beforeHyphen = b.newCharBox(before)

                            if hbox.width + beforeHyphen.width <= page.lineWidth then
                              lastBefore = beforeHyphen
                              lastAfter = after
                              longest()

                        longest()

                        if lastBefore ne null then
                          hbox add lastBefore
                          boxes.remove(0)
                          boxes.insert(0, b.newCharBox(lastAfter))
                    end match
                  case idx =>
                    val beforeHyphen = b.newCharBox(b.text.substring(0, idx + 1))

                    if hbox.width + beforeHyphen.width <= page.lineWidth then
                      hbox add beforeHyphen
                      boxes.remove(0)
                      boxes.insert(0, b.newCharBox(b.text.substring(idx + 1)))
              case _ =>

      line()

      if hbox.boxes.last.isSpace then hbox.boxes.remove(hbox.boxes.length - 1)
      if boxes.nonEmpty && boxes.head.isSpace then boxes.remove(0)
      if boxes.isEmpty then hbox add new HSpaceBox(2)

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

    val res = new ShiftBox(charBox(s), shift)

    font(f)
    res

  def charBox(s: String): CharBox = new CharBox(this, s, currentFont, currentColor)

  def draw(): Unit =
    page.set()
    page.draw(this, 0, 0)
    emit()
    page = pageFactory(this, pageWidth, pageHeight)

  def emit(): Unit

  def destroy(): Unit =
    ctx.destroy()
    surface.destroy()
end Compositor

class PDFCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    val pageWidth: Double,
    val pageHeight: Double,
    val pageFactory: (Compositor, Double, Double) => PageBox,
) extends Compositor:
  def emit(): Unit = ctx.showPage()

class PNGCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    path: String,
    val pageWidth: Double,
    val pageHeight: Double,
    val pageFactory: (Compositor, Double, Double) => PageBox,
) extends Compositor:
  def emit(): Unit = surface.writeToPNG(path)

object Compositor:
  val pointsPerInch = 72

  def pdf(
      path: String,
      widthIn: Int,
      heightIn: Int,
      pageFactory: (Compositor, Double, Double) => PageBox = (comp: Compositor, width: Double, height: Double) =>
        new SimplePage(width, height),
  ): Compositor =
    val surface = pdfSurfaceCreate(path, widthIn * pointsPerInch, heightIn * pointsPerInch)
    val context = surface.create

    new PDFCompositor(surface, context, widthIn * pointsPerInch, heightIn * pointsPerInch, pageFactory)

  def png(
      path: String,
      widthPx: Int,
      heightPx: Int,
      ppi: Double,
      pageFactory: (Compositor, Double, Double) => PageBox = (comp: Compositor, width: Double, height: Double) =>
        new SimplePage(width, height),
  ): Compositor =
    val pixelsPerPoint = ppi / pointsPerInch
    val surface = imageSurfaceCreate(Format.ARGB32, widthPx, heightPx)
    val context = surface.create

    context.scale(pixelsPerPoint, pixelsPerPoint)
    new PNGCompositor(surface, context, path, widthPx / pixelsPerPoint, heightPx / pixelsPerPoint, pageFactory)
end Compositor
