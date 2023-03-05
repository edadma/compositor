package io.github.edadma.compositor

case class Paper(widthInPoints: Double, heightInPoints: Double)

object Paper:
  def convert(width: Double, height: Double, units: Double => Double): Paper =
    Paper(units(width), units(height))

  def in(width: Double, height: Double): Paper = convert(width, height, Units.in)

  def mm(width: Double, height: Double): Paper = convert(width, height, Units.mm)

  val LETTER: Paper = in(8.5, 11)
  val A4: Paper = mm(210, 297)
