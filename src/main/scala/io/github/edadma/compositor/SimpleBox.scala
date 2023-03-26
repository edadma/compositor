package io.github.edadma.compositor

trait SimpleBox extends SetBox:
  val descent = 0

  def baselineAscent: Double = ascent

  def baselineHeight: Double = 0 // todo: was ascent, but didn't work for VSpaceBox
