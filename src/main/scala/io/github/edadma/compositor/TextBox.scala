package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

class TextBox(val text: String, val font: Font, val color: Color, val width: Double) extends Box:
  val height: Double = font.extents.height
  val ascent: Double = font.extents.ascent
  val descent: Double = font.extents.descent

  def draw(ctx: Context, x: Double, y: Double): Unit =
    if text.nonEmpty then
      ctx.moveTo(x, y)
      ctx.showText(text)
