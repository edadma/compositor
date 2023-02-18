package io.github.edadma.compositor

import scala.collection.mutable.ListBuffer

abstract class ListBox extends Box:
  protected val boxes = new ListBuffer[Box]

  protected def measure(b: Box): Double

  def +=(box: Box): Unit = boxes += box
