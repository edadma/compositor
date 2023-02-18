package io.github.edadma.compositor

class HBox extends ListBox:
  def height: Double = max(_.height)

  def descent: Double = max(_.descent)

  def width: Double = sum(_.width)
