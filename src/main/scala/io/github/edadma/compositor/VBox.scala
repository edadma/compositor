package io.github.edadma.compositor

abstract class VBox extends ListBox:
  protected def measure(b: Box): Double = b.baseline
