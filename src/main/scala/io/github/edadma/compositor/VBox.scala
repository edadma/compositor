package io.github.edadma.compositor

class VBox(toWidth: Option[Double] = None, toHeight: Option[Double] = None) extends ListBox:
  def height: Double = sum(_.height)

  def length: Double = height

  def ascent: Double = height // todo: not really correct

  def ascender: Double = height // todo: not really correct

  def descent: Double = if boxes.nonEmpty then boxes.last.descent else 0

  def descender: Double = if boxes.nonEmpty then boxes.last.descender else 0

  def width: Double = max(_.width)

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cy = y // todo: is this correct?

      for i <- boxes.indices do
        val b = boxes(i)

        if i == 0 then cy += b.ascent
        else
          val above = boxes(i - 1)

          cy += b.height - (b.descent - above.descent)

        b.draw(comp, x, cy)

  def setToWidth(width: Double): Unit = boxes foreach (_.setToWidth(width))

  def setToHeight(height: Double): Unit = set(height)
