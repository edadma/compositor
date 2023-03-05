package io.github.edadma.compositor

class VTop extends ListBox:
  def height: Double = sum(_.height)

  def length: Double = height

  def ascent: Double = if boxes.nonEmpty then boxes.head.ascent else 0

  def ascender: Double = if boxes.nonEmpty then boxes.head.ascender else 0

  def descent: Double = if boxes.nonEmpty then boxes.head.descent + boxes.tail.map(_.height).sum else 0

  def descender: Double = if boxes.nonEmpty then boxes.head.descender + boxes.tail.map(_.height).sum else 0

  def width: Double = max(_.width)

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cy = y

      for i <- boxes.indices do
        val b = boxes(i)

        b.draw(comp, x, cy)

        if i < boxes.length - 1 then
          val next = boxes(i + 1)

          cy += next.height - (next.descent - b.descent)

  def setToWidth(width: Double): Unit = boxes foreach (_.setToWidth(width))

  def setToHeight(height: Double): Unit = set(height)
