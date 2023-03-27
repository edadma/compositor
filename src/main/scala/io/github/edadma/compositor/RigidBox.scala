package io.github.edadma.compositor

class RigidBox(val width: Double = 0, val ascent: Double = 0) extends SimpleBox with EmptyBox:
  val typ: Type = Type.Horizontal
