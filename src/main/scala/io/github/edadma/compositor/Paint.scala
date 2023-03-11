package io.github.edadma.compositor

enum Paint:
  case Extreme(xo: Double, yo: Double) extends Paint
  case MoveTo(xo: Double, yo: Double) extends Paint
  case LineTo(xo: Double, yo: Double) extends Paint
  case Width(pts: Double) extends Paint
  case Color(c: io.github.edadma.compositor.Color) extends Paint
  case Stroke extends Paint
