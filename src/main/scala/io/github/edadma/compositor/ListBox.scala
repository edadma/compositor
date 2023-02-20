package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

abstract class ListBox extends Box:
  protected[compositor] val boxes = new ArrayBuffer[Box]

  protected def sum(measure: Box => Double): Double = boxes map measure sum

  protected def max(measure: Box => Double): Double = boxes map measure max

  def add(box: Box): Unit = boxes += box

  def +=(box: Box): Unit = add(box)

  def clear(): Unit = boxes.clear()
