package io.github.edadma.compositor

trait AddableBox extends Box:
  def add(box: Box): AddableBox
  def nonEmpty: Boolean
  def last: Box
  def lastOption: Option[Box]
  def update(index: Int, elem: Box): Unit
