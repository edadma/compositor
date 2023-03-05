package io.github.edadma.compositor

abstract class Box:
  def height: Double
  def ascent: Double
  def ascender: Double
  def descent: Double
  def descender: Double
  def width: Double
  def draw(comp: Compositor, x: Double, y: Double): Unit
  def setToWidth(width: Double): Unit
  def setToHeight(height: Double): Unit
  def isSpace: Boolean = this.isInstanceOf[SpaceBox]
