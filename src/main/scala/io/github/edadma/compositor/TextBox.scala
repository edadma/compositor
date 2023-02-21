package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

class TextBox(val text: String, val font: Font, val color: Color, ctx: Context) extends Box:
  val height: Double = font.extents.height
  val ascent: Double = font.extents.ascent
  val descent: Double = font.extents.descent
  val width: Double = ctx.textExtents(text).xAdvance

  def draw(ctx: Context, x: Double, y: Double): Unit =
    if text.nonEmpty then
      ctx.moveTo(x, y)
      ctx.showText(text)

  def newTextBox(s: String): TextBox =
    new TextBox(s, font, color, ctx)
