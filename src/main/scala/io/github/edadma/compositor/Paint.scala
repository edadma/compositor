package io.github.edadma.compositor

enum Paint:
  case MoveTo(xo: Double, yo: Double) extends Paint
  case LineTo(xo: Double, yo: Double) extends Paint
  case Stroke extends Paint
