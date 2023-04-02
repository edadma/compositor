package io.github.edadma.compositor

import io.github.edadma.libcairo.TextExtents

class CharBox(comp: Compositor, val text: String, val font: Font, val color: Color) extends SetBox:
  def this(comp: Compositor, text: String) = this(comp, text, comp.currentFont, comp.currentColor)

  val extents: TextExtents = comp.ctx.textExtents(text)
  val baselineHeight: Option[Double] = Some(font.height)
  val baselineAscent: Double = font.extents.ascent
  val ascent: Double = -extents.yBearing
  val descent: Double = extents.height + extents.yBearing
  val width: Double = extents.xAdvance
  val typ: Type = Type.Start

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if text.nonEmpty then
      comp.selectFont(font)
      comp.color(color)
      comp.ctx.moveTo(x, y)
      comp.ctx.showText(text)

  def newCharBox(s: String): CharBox = new CharBox(comp, s, font, color)

  override def toString: String = s"CharBox($height, $descent, \"$text\")"
