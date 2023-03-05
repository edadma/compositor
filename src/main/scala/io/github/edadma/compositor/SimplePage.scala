package io.github.edadma.compositor

class SimplePage(val lineWidth: Double, pageHeight: Option[Double] = None) extends VBox with PageBox:
  def set(): Unit =
    setToWidth(lineWidth)
    pageHeight foreach setToHeight

  override def setToWidth(width: Double): Unit = super.setToWidth(lineWidth)

  override def setToHeight(height: Double): Unit = pageHeight foreach super.setToHeight

def simplePageFactory(fixedHeight: Boolean = true): PageFactory =
  (comp: Compositor, width: Double, height: Double) => new SimplePage(width, Option.when(fixedHeight)(height))
