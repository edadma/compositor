package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

trait EmptyBox extends Box:
  def draw(comp: Compositor, x: Double, y: Double): Unit = {}
