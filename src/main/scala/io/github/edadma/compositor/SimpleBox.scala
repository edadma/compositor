package io.github.edadma.compositor

trait SimpleBox extends SetBox:
  def ascent: Double = height
  def ascender: Double = height

  val descent = 0
  val descender = 0
