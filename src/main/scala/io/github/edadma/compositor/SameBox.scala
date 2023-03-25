package io.github.edadma.compositor

trait SameBox(box: Box) extends SetBox:
  def height: Double = box.height

  def ascent: Double = box.ascent

  def descent: Double = box.descent

  def width: Double = box.width
