package io.github.edadma.compositor

import pprint.pprintln

class VTop extends ListBox:
  val typ: Type = Type.Vertical

  def pointLength: Double = /*_height getOrElse*/ (naturalAscent + naturalDescent)

  def naturalWidth: Double = max(_.width)

  def ascent: Double = naturalAscent

  def naturalAscent: Double = if boxes.nonEmpty then boxes.head.ascent else 0

  def descent: Double = _height.map(_ - naturalAscent) getOrElse naturalDescent

  def naturalDescent: Double =
    if nonEmpty then boxes.head.descent + boxes.tail.map(_.height).sum else 0

  def baselineAscent: Double = boxes.headOption map (_.baselineAscent) getOrElse 0

  def baselineHeight: Option[Double] = if nonEmpty then boxes.last.baselineHeight else None

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if nonEmpty then
      var cy = y

      for i <- boxes.indices do
        val b = boxes(i)

        b.draw(comp, x, cy)

        if i < boxes.length - 1 then cy += b.descent + boxes(i + 1).ascent

  def set(): Unit =
    boxes foreach { l =>
      l.setToWidth(width)
      l.set()
    }
    _height foreach set
