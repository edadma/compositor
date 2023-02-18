package io.github.edadma.compositor

class SpaceBox(min: Double) extends Box:
  val ascent: Double = 0
  val height: Double = 0
  val baseline: Double = 0
  var stretch: Double = 0

  def width: Double = min + stretch
