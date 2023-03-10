package io.github.edadma.compositor

case class Paper(width: Double, height: Double)

object Paper:
  def convert(width: Double, height: Double, units: Double => Double): Paper =
    Paper(units(width), units(height))

  def in(width: Double, height: Double): Paper = convert(width, height, Units.in)

  def mm(width: Double, height: Double): Paper = convert(width, height, Units.mm)

  val LETTER: Paper = in(8.5, 11)
  val LEGAL: Paper = in(8.5, 14)
  val TABLOID: Paper = in(11, 17)
  val A4: Paper = mm(210, 297)
  val A5: Paper = mm(148, 210)
  val A6: Paper = mm(105, 148)
  val A7: Paper = mm(74, 105)
