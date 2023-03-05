package io.github.edadma.compositor

class HBox extends ListBox:
  def height: Double = max(_.height)

  def ascent: Double = max(_.ascent)

  def ascender: Double = max(_.ascender)

  def descent: Double = max(_.descent)

  def descender: Double = max(_.descender)

  def width: Double = sum(_.width)

  def length: Double = width

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cx = x

      boxes foreach { b =>
        b.draw(comp, cx, y)
        cx += b.width
      }

  def setToWidth(width: Double): Unit = set(width)

  def setToHeight(height: Double): Unit = {}
