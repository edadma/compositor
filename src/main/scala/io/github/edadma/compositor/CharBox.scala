package io.github.edadma.compositor

import io.github.edadma.libcairo.TextExtents

case class CharBox(text: String, font: Font) extends Box:
  val extents: TextExtents
  val height: Double = extents.height
  val descent: Double = 0
  val width: Double = extents.width
