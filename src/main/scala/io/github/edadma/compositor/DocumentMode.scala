package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

class DocumentMode(protected val comp: Compositor) extends Mode:
  val pages = new ArrayBuffer[PageBox]

  def add(box: Box): Unit = pages += box.asInstanceOf[PageBox]

  def result: Box = ???
