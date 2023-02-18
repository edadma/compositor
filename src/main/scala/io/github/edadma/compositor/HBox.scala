package io.github.edadma.compositor

abstract class HBox extends ListBox:
  protected def measure(b: Box): Double = b.width
