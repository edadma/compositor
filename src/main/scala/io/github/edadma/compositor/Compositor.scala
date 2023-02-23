package io.github.edadma.compositor

import io.github.edadma.compositor
import io.github.edadma.libcairo.{Context, FontSlant, FontWeight, Surface, TextExtents, pdfSurfaceCreate}

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

class Compositor private (private[compositor] val surface: Surface, private[compositor] val ctx: Context):
  private val boxes = new ArrayBuffer[Box]
  private[compositor] var currentFont: Font = null
  private var currentColor: Color = new Color(0, 0, 0)
  private var page = new VBox

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

  def paragraph(width: Double): Unit =
    while boxes.nonEmpty do
      val hbox = new HBox

      @tailrec
      def line(): Unit =
        if boxes.nonEmpty then
          if hbox.width + boxes.head.width <= width then
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

                            if hbox.width + beforeHyphen.width <= width then
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

                    if hbox.width + beforeHyphen.width <= width then
                      hbox add beforeHyphen
                      boxes.remove(0)
                      boxes.insert(0, b.newTextBox(b.text.substring(idx + 1)))
              case _ =>

      line()

      if hbox.boxes.last.isSpace then hbox.boxes.remove(hbox.boxes.length - 1)
      if boxes.nonEmpty && boxes.head.isSpace then boxes.remove(0)
      if boxes.nonEmpty then hbox.set(width)

      page add hbox
    end while
  end paragraph

  def bold(): Unit = font(currentFont.family, currentFont.slant, FontWeight.BOLD, currentFont.size)

  def normal(): Unit = font(currentFont.family, currentFont.slant, FontWeight.NORMAL, currentFont.size)

  def size(points: Double): Unit = font(currentFont.family, currentFont.slant, currentFont.weight, points)

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

  def draw(): Unit =
    page.set(792)
    page.draw(this, 0, 0)
    ctx.showPage()

  def destroy(): Unit =
    ctx.destroy()
    surface.destroy()
end Compositor

object Compositor:
  def pdf(path: String, width: Double, height: Double): Compositor =
    val surface = pdfSurfaceCreate(path, width, height)
    val context = surface.create

    new Compositor(surface, context)
end Compositor
