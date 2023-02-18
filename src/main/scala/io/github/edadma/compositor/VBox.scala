package io.github.edadma.compositor

class VBox extends ListBox:
  protected def measure(b: Box): Double = b.baseline
