package io.github.edadma.compositor

trait SimpleBox extends Box:
  def ascent: Double = height
  def ascender: Double = height

  val descent = 0
