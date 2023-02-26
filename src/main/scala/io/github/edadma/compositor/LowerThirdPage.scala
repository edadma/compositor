package io.github.edadma.compositor

class LowerThirdPage(comp: Compositor, val lineWidth: Double, pageHeight: Double) extends VBox with PageBox:
  override def set(): Unit =
    setToWidth(lineWidth - 20)
    setToHeight(1.0 / 3 * pageHeight - 10)

  override def draw(comp: Compositor, x: Double, y: Double): Unit =
    comp.color(0, 0, 0, 0)
    comp.ctx.moveTo(x, y)
    comp.ctx.rectangle(x, y, lineWidth, 2.0 / 3 * pageHeight)
    comp.ctx.fill()
    comp.color(0, 0, 1)
    comp.ctx.moveTo(x, y + 2.0 / 3 * pageHeight)
    comp.ctx.rectangle(x, y, lineWidth, 1.0 / 3 * pageHeight)
    comp.ctx.fill()
    super.draw(comp, x + 10, y + 2.0 / 3 * pageHeight + 10)
    comp.ctx.setLineWidth(.5)
    comp.color(1, 0, 0)
    comp.ctx.moveTo(x, y)
    comp.ctx.rectangle(x, y, lineWidth, pageHeight)
    comp.ctx.stroke()

def lowerThirdPage(comp: Compositor, width: Double, height: Double) =
  new LowerThirdPage(comp, width, height)
