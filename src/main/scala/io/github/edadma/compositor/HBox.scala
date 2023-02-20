package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

class HBox extends ListBox:
  def height: Double = max(_.height)

  def ascent: Double = max(_.ascent)

  def descent: Double = max(_.descent)

  def width: Double = sum(_.width)

  def draw(ctx: Context, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cx = x

      boxes foreach { b =>
        b.draw(ctx, cx, y)
        cx += b.width
      }
