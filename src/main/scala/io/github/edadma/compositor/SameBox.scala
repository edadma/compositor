package io.github.edadma.compositor

trait SameBox(box: Box) extends SetBox:
  val typ: Type = box.typ

  def ascent: Double = box.ascent

  def descent: Double = box.descent

  def width: Double = box.width

  def baselineAscent: Double = box.baselineAscent

  def baselineHeight: Option[Double] = box.baselineHeight
