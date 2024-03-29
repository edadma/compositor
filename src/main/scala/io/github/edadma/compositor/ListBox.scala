package io.github.edadma.compositor

import pprint.pprintln

import scala.collection.mutable.ArrayBuffer

abstract class ListBox extends AbstractBox with AddableBox:
  def pointLength: Double

  protected[compositor] val boxes = new ArrayBuffer[Box]

  def isEmpty: Boolean = boxes.isEmpty

  def nonEmpty: Boolean = boxes.nonEmpty

  def last: Box = boxes.last

  def lastOption: Option[Box] = boxes.lastOption

  def length: Int = boxes.length

  def update(index: Int, elem: Box): Unit = boxes.update(index, elem)

  def remove(index: Int): Box = boxes.remove(index)

  protected def sum(measure: Box => Double): Double = boxes map measure sum

  protected def max(measure: Box => Double): Double = boxes map measure max

  def add(box: Box): ListBox =
    boxes += box
    this

  def +=(box: Box): Unit = add(box)

  def set(size: Double): Unit =
    val allSpaces = boxes.filter(_.isSpace).asInstanceOf[ArrayBuffer[SpaceBox]]

    if allSpaces.nonEmpty then
      val maxOrder = allSpaces.map(_.order).max
      val spaces = allSpaces.filter(_.order == maxOrder)
      val totalStretchable = spaces.map(_.stretchable).sum

      spaces foreach (s => s.stretch = 0)

      val diff = size - pointLength

      if totalStretchable > 0 then spaces foreach (s => s.stretch = diff * s.stretchable / totalStretchable)
