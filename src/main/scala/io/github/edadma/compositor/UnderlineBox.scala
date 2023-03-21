package io.github.edadma.compositor

class UnderlineBox(comp: Compositor, box: Box, thickness: Double = 0.8, color: Color = null) extends SameBox(box):
  val underlineColor: Color = Option(color) getOrElse comp.currentColor

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    box.draw(comp, x, y)
    comp.ctx.setLineWidth(thickness)
    comp.color(underlineColor)
    comp.ctx.moveTo(x, y + 1)
    comp.ctx.lineTo(x + width, y + 1)
    comp.ctx.stroke()
