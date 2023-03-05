package io.github.edadma.compositor

class SimplePage(val lineWidth: Double, pageHeight: Option[Double]) extends VBox with PageBox:
  def set(): Unit =
    setToWidth(lineWidth)
    pageHeight foreach setToHeight

def simplePageFactory(fixedHeight: Boolean = true): PageFactory =
  (comp: Compositor, width: Double, height: Double) => new SimplePage(width, Option.when(fixedHeight)(height))
