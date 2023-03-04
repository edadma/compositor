package io.github.edadma.compositor

class Color(val red: Double, val green: Double, val blue: Double, val alpha: Double = 1):
  def this(r: Int, g: Int, b: Int, a: Int) = this(r / 255.0, g / 255.0, b / 255.0, a / 255.0)
