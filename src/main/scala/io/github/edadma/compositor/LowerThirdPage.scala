package io.github.edadma.compositor

class LowerThirdPage(comp: Compositor, val lineWidth: Double, pageHeight: Double) extends VBox with PageBox:
  override def set(): Unit =
    setToWidth(lineWidth)
    setToHeight(pageHeight)

  override def draw(comp: Compositor, x: Double, y: Double): Unit =
    comp.ctx.setSourceRGBA(1, 0, 0, 1)
    comp.ctx.moveTo(x, y)
    comp.ctx.lineTo(x + 50, y)
    super.draw(comp, x, y)

def lowerThirdPage(comp: Compositor, width: Double, height: Double) =
  new LowerThirdPage(comp, width, height)
