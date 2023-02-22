package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

class ShiftBox(box: Box, shift: Double) extends SameBox(box):
  def draw(ctx: Context, x: Double, y: Double): Unit =
    box.draw(ctx, x, y + shift)
