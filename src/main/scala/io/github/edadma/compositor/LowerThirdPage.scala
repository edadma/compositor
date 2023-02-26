package io.github.edadma.compositor

class LowerThirdPage(comp: Compositor, val lineWidth: Double, pageHeight: Double) extends VBox with PageBox:
  override def set(): Unit =
    setToWidth(lineWidth)
    setToHeight(pageHeight)

  override def draw(comp: Compositor, x: Double, y: Double): Unit =
    comp.color(0, 0, 0, 0)
    comp.ctx.moveTo(x, y)
    comp.ctx.setLineWidth(.5)
    comp.ctx.rectangle(x, y, x + lineWidth, 2.0 / 3 * pageHeight)
    comp.ctx.fill()
    super.draw(comp, x, y)

def lowerThirdPage(comp: Compositor, width: Double, height: Double) =
  new LowerThirdPage(comp, width, height)
