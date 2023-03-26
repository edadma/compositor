package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

class BoxMode(protected val comp: Compositor) extends Mode:
  var contents: Box = null

  def add(box: Box): Unit =
    if contents ne null then sys.error("BoxMode full")

    contents = box

  def result: Box =
    if contents eq null then sys.error("BoxMode empty")

    contents

  override def done(): Unit = pop
