package io.github.edadma.compositor

class ShiftBox(box: Box, shift: Double) extends SetBox:
  def draw(comp: Compositor, x: Double, y: Double): Unit =
    box.draw(comp, x, y + shift)

  def height: Double = 0 // todo: fix this if possible

  def ascent: Double = 0

  def ascender: Double = 0

  def descent: Double = 0

  def descender: Double = 0

  def width: Double = box.width

  override def toString: String = s"ShiftBox($height, $descent, $box)"
