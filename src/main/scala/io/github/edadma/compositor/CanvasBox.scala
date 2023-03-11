package io.github.edadma.compositor

class CanvasBox(comp: Compositor, painting: List[Paint]) extends SimpleBox:
  private val cap = comp.currentLineCap
  private val join = comp.currentLineJoin
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
    case Paint.MoveTo(xo, yo) => point(xo, yo)
    case Paint.LineTo(xo, yo) => point(xo, yo)
    case _                    =>
  }

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    val ctx = comp.surface.create

    setColor(ctx, comp.currentColor)
    comp.setScale(ctx)

    import Paint._

    painting foreach {
      case MoveTo(xo, yo) => ctx.moveTo(x - x1 + xo, y + y1 - yo)
      case LineTo(xo, yo) => ctx.lineTo(x - x1 + xo, y + y1 - yo)
      case Width(pts)     => ctx.setLineWidth(pts)
      case Color(c)       => setColor(ctx, c)
      case Stroke         => ctx.stroke()
    }

    ctx.destroy()
