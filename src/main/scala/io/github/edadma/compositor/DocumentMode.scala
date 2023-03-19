package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

class DocumentMode(protected val comp: Compositor) extends Mode:
  val pages = new ArrayBuffer[PageBox]

  def add(box: Box): Unit =
    pages += box.asInstanceOf[PageBox]
  //    page = pageFactory(this, pageWidth, pageHeight)

  def done(): Unit =
    pages foreach { page =>
      page.set()
      page.draw(this, 0, page.ascent)
      comp.emit()
    }
