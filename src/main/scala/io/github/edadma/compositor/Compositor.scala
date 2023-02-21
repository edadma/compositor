package io.github.edadma.compositor

import io.github.edadma.compositor
import io.github.edadma.libcairo.{Context, FontSlant, FontWeight, Surface, TextExtents, pdfSurfaceCreate}

import scala.collection.mutable.ArrayBuffer

class Compositor private (surface: Surface, ctx: Context):
  private val boxes = new ArrayBuffer[Box]
  private var currentFont: Font = font("sans", FontSlant.NORMAL, FontWeight.NORMAL, 10)
  private var currentColor: Color = new Color(0, 0, 0)
  private var page = new VBox

  def addWord(text: String): Unit = addBox(textBox(text))

  def addText(text: String): Unit =
    val words = text.split(' ').filterNot(_ == "")

    words foreach addWord

  def addBox(box: Box): Unit =
    if boxes.nonEmpty then
      boxes.last match
        case b: TextBox =>
          if b.text.nonEmpty then
            boxes += new SpaceBox(
              if ".!?:" contains b.text.last then b.font.space * 1.5 else b.font.space,
            ) // todo: use font info for spaces
        case _ =>
    else boxes += new RigidBox(width = 36)

    boxes += box

  def setFont(f: Font): Unit =
    ctx.selectFontFace(f.family, f.slant, f.weight)
    ctx.setFontSize(f.size)
    currentFont = f

  def font(family: String, slant: FontSlant, weight: FontWeight, size: Double, set: Boolean = false): Font =
    ctx.selectFontFace(family, slant, weight)
    ctx.setFontSize(size)

    val TextExtents(_, _, _, _, _Width, _) = ctx.textExtents("_")
    val TextExtents(_, _, _, _, _sWithSpaceWidth, _) = ctx.textExtents("_ _")
    val extents = ctx.fontExtents

    val res = new Font(family, slant, weight, size, extents, _sWithSpaceWidth - 2 * _Width)

    if set then currentFont = res

    res

  def paragraph(width: Double): Unit =
    while boxes.nonEmpty do
      val hbox = new HBox

      while boxes.nonEmpty && hbox.width + boxes.head.width <= width do hbox add boxes.remove(0)
      if hbox.boxes.last.isSpace then hbox.boxes.remove(hbox.boxes.length - 1)
      if boxes.nonEmpty && boxes.head.isSpace then boxes.remove(0)

      if boxes.nonEmpty then
        val diff = width - hbox.width
        val spaces = hbox.boxes.filter(_.isSpace).asInstanceOf[ArrayBuffer[SpaceBox]]
        val stretch = diff / spaces.length

        spaces foreach (_.stretch = stretch)

      page add hbox
  end paragraph

  def textBox(s: String): TextBox = new TextBox(s, currentFont, currentColor, ctx)

//  def charBox(text: String): CharBox =
//    val extents = ctx textExtents text
//
//    new CharBox(text, currentFont, currentColor):
//      val height: Double = extents.height
//      val descent: Double = 0
//      val width: Double = extents.width

  def draw(): Unit =
    page.draw(ctx, 0, 0)
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
