package io.github.edadma.compositor

trait SimpleBox extends Box:
  def ascent: Double = height

  val descent = 0
