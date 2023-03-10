package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

trait PageBox extends AddableBox:
  def lineWidth: Double
  def add(box: Box): AddableBox
  def set(): Unit
