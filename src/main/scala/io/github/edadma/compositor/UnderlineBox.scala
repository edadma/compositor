package io.github.edadma.compositor

class UnderlineBox(box: Box, color: Color = Color("white"), thickness: Double = 0.8) extends SameBox(box):
  def draw(comp: Compositor, x: Double, y: Double): Unit =
    box.draw(comp, x, y)
    comp.ctx.setLineWidth(thickness)
    comp.setColor(color)
    comp.ctx.moveTo(x, y + 1)
    comp.ctx.lineTo(x + width, y + 1)
    comp.ctx.stroke()
