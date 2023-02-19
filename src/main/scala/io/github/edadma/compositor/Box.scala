package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

abstract class Box:
  def height: Double
  def descent: Double
  def width: Double
  def draw(ctx: Context): Unit
