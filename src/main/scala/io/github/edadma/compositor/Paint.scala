package io.github.edadma.compositor

enum Paint:
  case Extreme(xo: Double, yo: Double) extends Paint
  case MoveTo(xo: Double, yo: Double) extends Paint
  case LineTo(xo: Double, yo: Double) extends Paint
  case Width(pts: Double) extends Paint
  case Color(c: io.github.edadma.compositor.Color) extends Paint
  case Stroke extends Paint
  case Fill extends Paint
  case Box(box: io.github.edadma.compositor.Box, xo: Double, yo: Double, origin: Origin) extends Paint
  case Arc(xo: Double, yo: Double, radius: Double, angle1: Double, angle2: Double) extends Paint

type Origin = "center" | "baseline"
