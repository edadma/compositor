package io.github.edadma.compositor
import io.github.edadma.libcairo.{Context, TextExtents}

class TextBox(val text: String, val font: Font, val color: Color, ctx: Context) extends Box:
  val extents: TextExtents = ctx.textExtents(text)
  val height: Double = font.extents.height
  val ascent: Double = font.extents.ascent
  val ascender: Double = -extents.yBearing
  val descent: Double = font.extents.descent
  val width: Double = extents.xAdvance

  def draw(ctx: Context, x: Double, y: Double): Unit =
    if text.nonEmpty then
      ctx.moveTo(x, y)
      ctx.showText(text)

  def newTextBox(s: String): TextBox = new TextBox(s, font, color, ctx)
