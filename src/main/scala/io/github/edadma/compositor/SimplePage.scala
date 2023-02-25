package io.github.edadma.compositor

class SimplePage(val lineWidth: Double, pageHeight: Double) extends VBox with PageBox:
  override def set(): Unit =
    setToWidth(lineWidth)
    setToHeight(pageHeight)
