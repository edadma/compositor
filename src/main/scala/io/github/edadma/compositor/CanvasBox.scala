package io.github.edadma.compositor

import io.github.edadma.libcairo.{LineCap, LineJoin}

class CanvasBox(comp: Compositor, painting: List[Paint]) extends SimpleBox:
  import Paint._

  private val color = comp.currentColor
  private var x1 = Double.MaxValue
  private var y1 = Double.MaxValue
  private var x2 = Double.MinValue
  private var y2 = Double.MinValue

  def width: Double = x2 - x1
  def height: Double = y2 - y1

  def point(x: Double, y: Double): Unit =
    if x < x1 then x1 = x
    if x > x2 then x2 = x
    if y < y1 then y1 = y
    if y > y2 then y2 = y

  painting foreach {
    case Extreme(xo, yo) => point(xo, yo)
    case MoveTo(xo, yo)  => point(xo, yo)
    case LineTo(xo, yo)  => point(xo, yo)
    case _               =>
  }

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    comp.ctx.setLineCap(LineCap.BUTT)
    comp.ctx.setLineJoin(LineJoin.MITER)
    comp.ctx.setLineWidth(2)
    comp.color(color)

    painting foreach {
      case MoveTo(xo, yo) => comp.ctx.moveTo(x - x1 + xo, y + y1 - yo)
      case LineTo(xo, yo) => comp.ctx.lineTo(x - x1 + xo, y + y1 - yo)
      case Width(pts)     => comp.ctx.setLineWidth(pts)
      case Color(c)       => comp.color(c)
      case Stroke         => comp.ctx.stroke()
      case _              =>
    }
