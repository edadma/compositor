package io.github.edadma.compositor

trait Box:
  def height: Double = ascent + descent
  def width: Double
  def ascent: Double
  def descent: Double
  def baselineAscent: Double
  def baselineHeight: Double
  def draw(comp: Compositor, x: Double, y: Double): Unit
  def setToWidth(width: Double): Unit
  def setToHeight(height: Double): Unit
  def set(): Unit
  def isSpace: Boolean = this.isInstanceOf[SpaceBox]
  def isHorizontal: Boolean = false
