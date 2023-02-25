package io.github.edadma.compositor
import io.github.edadma.libcairo.{Context, TextExtents}

class TextBox(comp: Compositor, val text: String, val font: Font, val color: Color) extends SetBox:
  comp.font(font)

  val extents: TextExtents = comp.ctx.textExtents(text)
  val height: Double = font.extents.height
  val ascent: Double = font.extents.ascent
  val ascender: Double = -extents.yBearing
  val descent: Double = font.extents.descent
  val width: Double = extents.xAdvance

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    comp.font(font)

    if text.nonEmpty then
      comp.ctx.moveTo(x, y)
      comp.ctx.showText(text)

  def newTextBox(s: String): TextBox = new TextBox(comp, s, font, color)
