package io.github.edadma.compositor

class CanvasBox(painting: List[Paint]) extends SimpleBox:
  val width: Double = 50
  val height: Double = 50

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    painting foreach {
      case Paint.MoveTo(xo, yo) => comp.ctx.moveTo(x + xo, y + yo)
      case Paint.LineTo(xo, yo) => comp.ctx.lineTo(x + xo, y + yo)
      case Paint.Stroke         => comp.ctx.stroke()
    }
