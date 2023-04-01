package io.github.edadma.compositor

class VBox() extends ListBox:
  val typ: Type = Type.Vertical

  def length: Double = naturalAscent + naturalDescent

  def naturalWidth: Double = max(_.width)

  def ascent: Double = _height.map(_ - naturalDescent) getOrElse naturalAscent

  def naturalAscent: Double = if boxes.nonEmpty then boxes.init.map(_.height).sum + boxes.last.ascent else 0

  def descent: Double = naturalDescent

  def naturalDescent: Double = boxes.lastOption map (_.descent) getOrElse 0

  def baselineAscent: Double = if boxes.nonEmpty then boxes.init.map(_.height).sum + boxes.last.baselineAscent else 0

  def baselineHeight: Option[Double] = max(_.baselineHeight) // todo: this is probably not correct

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

  def set(): Unit =
    boxes foreach (_.setToWidth(width))
    _height foreach set
