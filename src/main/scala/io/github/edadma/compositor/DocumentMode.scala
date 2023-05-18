package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

class DocumentMode(protected val comp: Compositor) extends Mode:
  def add(box: Box): Unit = comp.pages += box.asInstanceOf[PageBox]

  override def done(): Unit =
    comp.pages.last.add(new VSpaceBox(2, 0, 1))
    pop

  def result: Box = ???
