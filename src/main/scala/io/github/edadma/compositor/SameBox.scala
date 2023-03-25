package io.github.edadma.compositor

trait SameBox(box: Box) extends SetBox:
  def ascent: Double = box.ascent

  def descent: Double = box.descent

  def width: Double = box.width

  def baselineAscent: Double = box.baselineAscent

  def baselineHeight: Double = box.baselineHeight
