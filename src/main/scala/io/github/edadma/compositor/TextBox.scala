package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

abstract class TextBox private[compositor] (val text: String, val font: Font, val color: Color) extends Box:
  def draw(ctx: Context): Unit =
    if text.nonEmpty then
      println(s"TextBox '$text'")
      ctx.save()
      ctx.showText(text)
      ctx.restore()
