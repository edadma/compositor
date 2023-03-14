package io.github.edadma.compositor

import scala.collection.mutable.ListBuffer

class DocumentMode(comp: Compositor) extends Mode:
  val pages = new ListBuffer[PageBox]

  def add(box: Box): Unit = pages add box

  def done(): Unit = sys.error("can't call DocumentMode.done()")
