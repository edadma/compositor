package io.github.edadma.compositor
import io.github.edadma.libcairo.Context

class VBox extends ListBox:
  def height: Double = sum(_.height)

  def ascent: Double = height // todo: not really correct

  def ascender: Double = height // todo: not really correct

  def descent: Double = if boxes.nonEmpty then boxes.last.descent else 0

  def width: Double = max(_.width)

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cy = y
      var first = true

      boxes foreach { b =>
        if first then
          first = false
          cy += b.ascent
        else cy += b.height

        b.draw(comp, x, cy)
      }
