package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

abstract class Box:
  def height: Double
  def ascent: Double
  def ascender: Double
  def descent: Double
  def width: Double
  def draw(comp: Compositor, x: Double, y: Double): Unit
  def isSpace: Boolean = this.isInstanceOf[HSpaceBox]
  def isText: Boolean = this.isInstanceOf[TextBox]
