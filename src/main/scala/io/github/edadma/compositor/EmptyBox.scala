package io.github.edadma.compositor

trait EmptyBox extends Box:
  def draw(comp: Compositor, x: Double, y: Double): Unit = {}
