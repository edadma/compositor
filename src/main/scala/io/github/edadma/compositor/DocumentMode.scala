package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

class DocumentMode(protected val comp: Compositor) extends Mode:
  val pages = new ArrayBuffer[Box]

  def add(box: Box): Unit = pages += box

  def done(): Unit = sys.error("can't call DocumentMode.done()")
