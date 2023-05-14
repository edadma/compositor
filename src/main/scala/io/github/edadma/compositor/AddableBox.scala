package io.github.edadma.compositor

trait AddableBox extends Box:
  def add(box: Box): AddableBox
  def isEmpty: Boolean
  def nonEmpty: Boolean
  def last: Box
  def lastOption: Option[Box]
  def length: Int
  def update(index: Int, elem: Box): Unit
  def remove(index: Int): Box
