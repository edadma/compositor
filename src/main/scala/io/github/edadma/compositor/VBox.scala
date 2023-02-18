package io.github.edadma.compositor

class VBox extends ListBox:
  def height: Double = sum(_.height)

  def descent: Double = if boxes.nonEmpty then boxes.last.descent else 0

  def width: Double = max(_.width)
