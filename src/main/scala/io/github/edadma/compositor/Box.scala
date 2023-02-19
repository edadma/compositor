package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

abstract class Box:
  def height: Double
  def ascent: Double
  def descent: Double
  def width: Double
  def draw(ctx: Context, x: Double, y: Double): Unit
