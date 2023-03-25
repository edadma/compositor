package io.github.edadma.compositor

class ShiftBox(box: Box, shift: Double) extends SetBox:
  def draw(comp: Compositor, x: Double, y: Double): Unit =
    box.draw(comp, x, y + shift)

  def ascent: Double = 0 // todo: fix

  def descent: Double = 0 // todo: fix

  def baselineAscent: Double = 0 // todo: fix

  def baselineHeight: Double = 0 // todo: fix

  def width: Double = box.width

  override def toString: String = s"ShiftBox($height, $descent, $box)"
