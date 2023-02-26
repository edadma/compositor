package io.github.edadma.compositor

class SimplePage(val lineWidth: Double, pageHeight: Double, background: Option[Color]) extends VBox with PageBox:
  override def set(): Unit =
    setToWidth(lineWidth)
    setToHeight(pageHeight)

  override def draw(comp: Compositor, x: Double, y: Double): Unit =
    if background.isDefined then
      comp.color(background.get)
      comp.ctx.rectangle(x, y, lineWidth, pageHeight)
      comp.ctx.fill()

    super.draw(comp, x, y)

def simplePage(background: Option[Color] = None): PageFactory =
  (comp: Compositor, width: Double, height: Double) => new SimplePage(width, height, background)
