package io.github.edadma.compositor

import io.github.edadma.libcairo.TextExtents

case class TextBox(text: String, font: Font) extends Box:
  val extents: TextExtents
  val height: Double = font.extents.height
  val descent: Double = font.extents.descent
  val width: Double = extents.width // todo: may also include xBearing and/or xAdvance. not sure
