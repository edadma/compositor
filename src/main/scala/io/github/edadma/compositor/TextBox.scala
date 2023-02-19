package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

abstract class TextBox private[compositor] (val text: String, val font: Font, val color: Color) extends Box:
  def draw(ctx: Context, x: Double, y: Double): Unit =
    if text.nonEmpty then
      ctx.moveTo(x, y)
      ctx.showText(text)
