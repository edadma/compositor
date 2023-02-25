package io.github.edadma.compositor

import scala.collection.mutable.ArrayBuffer

trait PageBox extends Box:
  def lineWidth: Double
  def add(box: Box): Unit
  def set(): Unit
