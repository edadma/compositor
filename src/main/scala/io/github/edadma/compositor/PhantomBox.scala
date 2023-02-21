package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

class PhantomBox(box: Box) extends SameBox(box):
  def draw(ctx: Context, x: Double, y: Double): Unit = {}
