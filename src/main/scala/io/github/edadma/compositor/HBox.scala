package io.github.edadma.compositor

class HBox extends ListBox:
  protected def measure(b: Box): Double = b.width
