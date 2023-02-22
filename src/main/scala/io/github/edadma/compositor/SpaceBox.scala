package io.github.edadma.compositor

abstract class SpaceBox extends SimpleBox with EmptyBox:
  val order: Int
  var stretch: Double = 0
