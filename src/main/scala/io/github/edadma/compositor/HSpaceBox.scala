package io.github.edadma.compositor

class HSpaceBox(val order: Int, min: Double = 0) extends SpaceBox:
  val height: Double = 0

  def width: Double = min + stretch
