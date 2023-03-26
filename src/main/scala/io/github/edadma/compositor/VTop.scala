package io.github.edadma.compositor

class VTop extends ListBox:
  def length: Double = naturalAscent + naturalDescent

  def naturalWidth: Double = max(_.width)

  def ascent: Double = naturalAscent

  def naturalAscent: Double = if boxes.nonEmpty then boxes.head.ascent else 0

  def descent: Double = _height.map(_ - naturalAscent) getOrElse naturalDescent

  def naturalDescent: Double = if boxes.nonEmpty then boxes.head.descent + boxes.tail.map(_.baselineHeight).sum else 0

  def baselineAscent: Double = boxes.headOption map (_.baselineAscent) getOrElse 0

  def baselineHeight: Double = _height getOrElse sum(_.baselineHeight) // todo: this is probably not correct

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cy = y

      for i <- boxes.indices do
        val b = boxes(i)

        b.draw(comp, x, cy)

        if i < boxes.length - 1 then
          val next = boxes(i + 1)

//          cy += next.height - (next.descent - b.descent) // todo: this is wrong. baseline?
          cy += b.baselineHeight

  def set(): Unit =
    boxes foreach (_.set())
    _height foreach set
