package io.github.edadma.compositor

class VSpaceBox(val order: Int, min: Double = 0, val stretchable: Double = 1) extends SpaceBox:
  val width: Double = 0

  def height: Double = min + stretch
