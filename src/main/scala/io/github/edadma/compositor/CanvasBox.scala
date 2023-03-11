package io.github.edadma.compositor

class CanvasBox(painting: List[Paint]) extends SimpleBox:
  var x1: Double = Double.MaxValue
  var y1: Double = Double.MaxValue
  var x2: Double = Double.MinValue
  var y2: Double = Double.MinValue

  def width: Double = x2 - x1
  def height: Double = y2 - y1

  def point(x: Double, y: Double): Unit =
    if x < x1 then x1 = x
    if x > x2 then x2 = x
    if y < y1 then y1 = y
    if y > y2 then y2 = y

  painting foreach {
    case Paint.MoveTo(xo, yo) => point(xo, yo)
    case Paint.LineTo(xo, yo) => point(xo, yo)
    case Paint.Stroke         =>
  }

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    painting foreach {
      case Paint.MoveTo(xo, yo) => comp.ctx.moveTo(x - x1 + xo, y + y1 - yo)
      case Paint.LineTo(xo, yo) => comp.ctx.lineTo(x - x1 + xo, y + y1 - yo)
      case Paint.Stroke         => comp.ctx.stroke()
    }
