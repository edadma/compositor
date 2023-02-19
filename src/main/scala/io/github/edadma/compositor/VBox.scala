package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

class VBox extends ListBox:
  def height: Double = sum(_.height)

  def descent: Double = if boxes.nonEmpty then boxes.last.descent else 0

  def width: Double = max(_.width)

  def draw(ctx: Context): Unit =
    if boxes.nonEmpty then
      println("vbox")
      ctx.save()
      boxes foreach { b =>
        println(b.height)
        ctx.relMoveTo(0, b.height)
        b.draw(ctx)
      }
      ctx.restore()
