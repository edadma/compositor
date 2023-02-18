package io.github.edadma.compositor

class SpaceBox(min: Double) extends SimpleBox:
  val height: Double = 0
  var stretch: Double = 0

  def width: Double = min + stretch
