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
    case Box(box, xo, yo, "center") =>
      point(xo - box.width / 2, yo - box.height / 2)
      point(xo + box.width / 2, yo + box.height / 2)
    case Arc(xo, yo, radius, angle1, angle2) =>
      // todo: assumes a circle - check which quadrants the arc is in
      point(xo - radius, yo - radius)
      point(xo + radius, yo + radius)
    case _ =>
  }

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    comp.ctx.setLineCap(LineCap.BUTT)
    comp.ctx.setLineJoin(LineJoin.MITER)
    comp.ctx.setLineWidth(2)
    comp.color(color)

    painting foreach {
      case MoveTo(xo, yo)             => comp.ctx.moveTo(x - x1 + xo, y + y1 - yo)
      case LineTo(xo, yo)             => comp.ctx.lineTo(x - x1 + xo, y + y1 - yo)
      case Width(pts)                 => comp.ctx.setLineWidth(pts)
      case Color(c)                   => comp.color(c)
      case Stroke                     => comp.ctx.stroke()
      case Fill                       => comp.ctx.fill()
      case Box(box, xo, yo, "center") => box.draw(comp, x - x1 + xo - box.width / 2, y + y1 - yo + box.height / 2)
      case Arc(xo, yo, radius, angle1, angle2) => comp.ctx.arc(x - x1 + xo, y + y1 - yo, radius, angle1, angle2)
      case _                                   =>
    }
