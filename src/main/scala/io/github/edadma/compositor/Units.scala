package io.github.edadma.compositor

object Units:
  val POINTS_PER_INCH = 72.0
  private val CM_PER_INCH = 2.54
  private val INCH_PER_CM = 1 / CM_PER_INCH
  private val MM_PER_CM = 10.0
  private val CM_PER_MM = 1 / MM_PER_CM

  def in(x: Double): Double = x * POINTS_PER_INCH

  def cm(x: Double): Double = in(x * INCH_PER_CM)

  def mm(x: Double): Double = cm(x * CM_PER_MM)
