package io.github.edadma.compositor

trait SetBox extends Box:
  def setToWidth(width: Double): Unit = {}

  def setToHeight(height: Double): Unit = {}

  def set(): Unit = {}
