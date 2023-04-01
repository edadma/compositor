package io.github.edadma.compositor

// todo: this class may no longer be needed since VTop sets its lines to its width
trait PageBox extends AddableBox:
  def lineWidth: Double
