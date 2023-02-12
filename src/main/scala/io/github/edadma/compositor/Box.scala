package io.github.edadma.compositor

trait Box:
  def height: Double
  def ascent: Double
  def width: Double
  def descent: Double = height - ascent
