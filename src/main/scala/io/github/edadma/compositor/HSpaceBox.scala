package io.github.edadma.compositor

class HSpaceBox(val order: Int, min: Double = 0, val stretchable: Double = 1) extends SpaceBox:
  val typ: Type = Type.Start

  val ascent: Double = 0

  def width: Double = min + stretch

  override def toString: String = s"HSpaceBox($width)"
