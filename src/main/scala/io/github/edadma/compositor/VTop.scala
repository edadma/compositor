package io.github.edadma.compositor

import pprint.pprintln

class VTop extends ListBox:
  val typ: Type = Type.Vertical

  def length: Double = /*_height getOrElse*/ (naturalAscent + naturalDescent)

  def naturalWidth: Double = max(_.width)

  def ascent: Double = naturalAscent

  def naturalAscent: Double = if boxes.nonEmpty then boxes.head.ascent else 0

  def descent: Double = _height.map(_ - naturalAscent) getOrElse naturalDescent

  def naturalDescent: Double =
    if boxes.nonEmpty then boxes.head.descent + boxes.tail.map(b => b.baselineHeight max b.height).sum else 0

  def baselineAscent: Double = boxes.headOption map (_.baselineAscent) getOrElse 0

  def baselineHeight: Double = 0
//    _height getOrElse sum(b => b.baselineHeight max b.height) // todo: this is probably not correct

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cy = y

      for i <- boxes.indices do
        val b = boxes(i)

        b.draw(comp, x, cy)

        if i < boxes.length - 1 then
          val next = boxes(i + 1)

//          cy += next.height - (next.descent - b.descent) // todo: this is wrong. baseline?
          cy += {
            val skip = b.descent + next.ascent
            val baselines = List(b.baselineHeight, next.baselineHeight) filterNot (_ == 0)
            val baseline = baselines.sum / baselines.length

            skip max baseline
          }

  def set(): Unit =
    boxes foreach (_.set())
    _height foreach set
