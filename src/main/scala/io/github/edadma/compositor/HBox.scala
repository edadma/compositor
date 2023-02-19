package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

class HBox extends ListBox:
  def height: Double = max(_.height)

  def descent: Double = max(_.descent)

  def width: Double = sum(_.width)

  def draw(ctx: Context): Unit =
    if boxes.nonEmpty then
      println("hbox")
      ctx.save()
      boxes foreach { b =>
        b.draw(ctx)
        ctx.relMoveTo(b.width, 0)
      }
      ctx.restore()
