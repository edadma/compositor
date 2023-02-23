package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

abstract class ListBox extends Box:
  protected[compositor] val boxes = new ArrayBuffer[Box]

  protected def sum(measure: Box => Double): Double = boxes map measure sum

  protected def max(measure: Box => Double): Double = boxes map measure max

  def add(box: Box): Unit = boxes += box

  def +=(box: Box): Unit = add(box)

  def clear(): Unit = boxes.clear()

  def set(size: Double): Unit =
    val diff = size - width
    val allSpaces = boxes.filter(_.isSpace).asInstanceOf[ArrayBuffer[SpaceBox]]

    if allSpaces.nonEmpty then
      val maxOrder = allSpaces.map(_.order).max
      val spaces = allSpaces.filter(_.order == maxOrder)
      val stretch = diff / spaces.length

      spaces foreach (_.stretch = stretch)
