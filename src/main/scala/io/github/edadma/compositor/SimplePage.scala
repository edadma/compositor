package io.github.edadma.compositor

class SimplePage(val lineWidth: Double, pageHeight: Option[Double] = None) extends VTop with PageBox:
  setToWidth(lineWidth)
  pageHeight foreach setToHeight

def simplePageFactory(fixedHeight: Boolean = true): PageFactory =
  (comp: Compositor, width: Double, height: Double) => new SimplePage(width, Option.when(fixedHeight)(height))
