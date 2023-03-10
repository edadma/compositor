package io.github.edadma.compositor

class UnderlineBox(box: Box) extends SameBox(box):
  def draw(comp: Compositor, x: Double, y: Double): Unit =
    box.draw(comp, x, y)
    comp.ctx.setLineWidth(1)
    comp.color(comp.currentColor)
    comp.ctx.moveTo(x, y + 1)
    comp.ctx.lineTo(x + width, y + 1)
