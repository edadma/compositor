package io.github.edadma.compositor

import io.github.edadma.libcairo.TextExtents

class CharBox(comp: Compositor, val text: String, val font: Font, val color: Color) extends SetBox:
  comp.selectFont(font)

  val extents: TextExtents = comp.ctx.textExtents(text)
  val height: Double = font.extents.height
  val ascent: Double = font.extents.ascent
  val ascender: Double = -extents.yBearing
  val descent: Double = font.extents.descent
  val descender: Double = extents.height + extents.yBearing
  val width: Double = extents.xAdvance

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if text.nonEmpty then
      comp.selectFont(font)
      comp.color(color)
      comp.ctx.moveTo(x, y)
      comp.ctx.showText(text)
//      comp.ctx.moveTo(x, y)
//      comp.ctx.lineTo(x + extents.xAdvance, y)
//      comp.ctx.stroke()

  def newCharBox(s: String): CharBox = new CharBox(comp, s, font, color)
