package io.github.edadma.compositor

import io.github.edadma.libcairo.TextExtents

class GlyphBox(comp: Compositor, val text: String, val font: Font, val color: Color) extends SetBox:
  def this(comp: Compositor, text: String) = this(comp, text, comp.currentFont, comp.currentColor)

  val extents: TextExtents = comp.ctx.textExtents(text)
  val height: Double = extents.height
  val ascent: Double = extents.height
  val ascender: Double = extents.height
  val descent: Double = 0
  val descender: Double = 0
  val width: Double = extents.xAdvance - extents.xBearing

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if text.nonEmpty then
      comp.selectFont(font)
      comp.color(color)
      comp.ctx.moveTo(x - extents.xBearing, y - (extents.height + extents.yBearing))
      comp.ctx.showText(text)

  override def toString: String = s"GlyphBox($height, \"$text\")"
