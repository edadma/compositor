package io.github.edadma.compositor

class VSpaceBox(val order: Int, min: Double = 0, val stretchable: Double = 1) extends SpaceBox:
  val typ: Type = Type.Vertical

  val width: Double = 0

  def ascent: Double = min + stretch

  override def toString: String = s"VSpaceBox(order = $order, ascent = $ascent)"
