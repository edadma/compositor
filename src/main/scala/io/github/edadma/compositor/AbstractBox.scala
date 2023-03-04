package io.github.edadma.compositor

abstract class AbstractBox extends Box:
  var background: Color | Null = null
  var tightBackground: Boolean = false

  def paint(comp: Compositor, x: Double, y: Double): Unit =
    if background ne null then
      comp.color(background)

      if tightBackground then comp.ctx.rectangle(x, y - ascender, width, ascender + descender)
      else comp.ctx.rectangle(x, y - ascent, width, height)

      comp.ctx.fill()

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    paint(comp, x, y)
