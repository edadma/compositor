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

          boxes += box
        case _ =>
          boxes += box
    else boxes += box

  def font(f: Font): Unit =
    ctx.selectFontFace(f.family, f.slant, f.weight)
    ctx.setFontSize(f.size)

  def font(family: String, slant: FontSlant, weight: FontWeight, size: Double): Font =
    ctx.selectFontFace(family, slant, weight)
    ctx.setFontSize(size)

    val TextExtents(_, _, _Width, _, _, _) = ctx.textExtents("_")
    val TextExtents(_, _, _sWithSpaceWidth, _, _, _) = ctx.textExtents("_ _")
    val extents = ctx.fontExtents

    new Font(family, slant, weight, size, extents, _sWithSpaceWidth - 2 * _Width)
  def paragraph(width: Double): Unit =
    val hbox = new HBox

    boxes foreach hbox.add
    page add hbox
    boxes.clear()
  end paragraph

  def textBox(s: String): TextBox =
    val extents = ctx textExtents s

    new TextBox(s, currentFont, currentColor):
      val height: Double = currentFont.extents.height
      val ascent: Double = currentFont.extents.ascent
      val descent: Double = currentFont.extents.descent
      val width: Double = extents.width // todo: may also include xBearing and/or xAdvance. not sure

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
