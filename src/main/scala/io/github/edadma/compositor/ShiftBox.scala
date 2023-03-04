package io.github.edadma.compositor

class ShiftBox(box: Box, shift: Double) extends SameBox(box):
  def draw(comp: Compositor, x: Double, y: Double): Unit =
    box.draw(comp, x, y + shift)
