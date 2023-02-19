package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

class VBox extends ListBox:
  def height: Double = sum(_.height)

  def descent: Double = if boxes.nonEmpty then boxes.last.descent else 0

  def width: Double = max(_.width)

  def draw(ctx: Context, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cy = y

      boxes foreach { b =>
        cy += b.height
        b.draw(ctx, x, cy)
      }
