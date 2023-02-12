package io.github.edadma.compositor

import scala.collection.mutable.ListBuffer

abstract class ListBox extends Box:
  val list = new ListBuffer[Box]

  protected def measure(b: Box): Double
