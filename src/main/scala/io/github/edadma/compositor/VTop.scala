package io.github.edadma.compositor

class VTop(toWidth: Option[Double] = None, toHeight: Option[Double] = None) extends ListBox:
  def height: Double = toHeight getOrElse sum(_.height)

  def length: Double = height

  def ascent: Double = if boxes.nonEmpty then boxes.head.ascent else 0

  def ascender: Double = if boxes.nonEmpty then boxes.head.ascender else 0

  def descent: Double =
    toHeight match
      case None    => if boxes.nonEmpty then boxes.head.descent + boxes.tail.map(_.height).sum else 0
      case Some(h) => h - ascent

  def descender: Double =
    toHeight match
      case None    => if boxes.nonEmpty then boxes.head.descender + boxes.tail.map(_.height).sum else 0
      case Some(h) => h - ascender

  def width: Double = toWidth getOrElse max(_.width)

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cy = y

      for i <- boxes.indices do
        val b = boxes(i)

        b.draw(comp, x, cy)

        if i < boxes.length - 1 then
          val next = boxes(i + 1)

          println(b)
          println((b.descent, b.height))
          cy += next.height - (next.descent - b.descent)

  def setToWidth(width: Double): Unit = boxes foreach (_.setToWidth(width))

  def setToHeight(height: Double): Unit = set(height)
