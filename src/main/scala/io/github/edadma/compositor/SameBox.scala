package io.github.edadma.compositor

abstract class SameBox(box: Box) extends Box:
  def height: Double = box.height

  def ascent: Double = box.ascent

  def ascender: Double = box.ascender

  def descent: Double = box.descent

  def width: Double = box.width
