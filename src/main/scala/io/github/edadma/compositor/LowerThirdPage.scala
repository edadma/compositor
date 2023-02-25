package io.github.edadma.compositor

class LowerThirdPage(val lineWidth: Double, pageHeight: Double) extends VBox with PageBox:
  override def set(): Unit =
    setToWidth(lineWidth)
    setToHeight(pageHeight)

def lowerThirdPagecomp(Compositor, width: Double, height: Double)