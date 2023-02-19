package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

trait EmptyBox extends Box:
  def draw(ctx: Context, x: Double, y: Double): Unit = {}
