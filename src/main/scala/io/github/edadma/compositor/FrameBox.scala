package io.github.edadma.compositor

import pprint.pprintln

class FrameBox(box: Box) extends Box:
  var rounded: Boolean = true
  var background: Color | Null = null
  var border: Color | Null = null
  var borderWidth: Double = 0.5
  var cornerRadius: Double = 2
  var topPadding: Double = 2
  var bottomPadding: Double = 2
  var leftPadding: Double = 2
  var rightPadding: Double = 2

  def padding(pts: Double): Unit =
    topPadding = pts
    bottomPadding = pts
    leftPadding = pts
    rightPadding = pts

  def paint(comp: Compositor, x: Double, y: Double): Unit =
    def frame(): Unit =
      if rounded then
        util.roundedRectanglePath(
          comp.ctx,
          x,
          y - ascent,
          width,
          height,
          cornerRadius,
        )
      else
        comp.ctx.rectangle(
          x,
          y - ascent,
          width,
          height,
        )

    if background ne null then
      comp.color(background)
      frame()
      comp.ctx.fill()

    if border ne null then
      comp.color(border)
      comp.ctx.setLineWidth(borderWidth)
      frame()
      comp.ctx.stroke()

  val typ: Type = box.typ

  def width: Double = box.width + leftPadding + rightPadding

  def ascent: Double = box.ascent + topPadding

  def descent: Double = box.descent + bottomPadding

  def baselineAscent: Double = box.baselineAscent // todo: left out + topPadding

  override def baselineHeight: Option[Double] = box.baselineHeight + topPadding // todo: left out + topPadding + bottomPadding

  def setToWidth(width: Double): Unit = box.setToWidth(width - leftPadding - rightPadding)

  def setToHeight(height: Double): Unit = box.setToHeight(height - topPadding - bottomPadding)

  def set(): Unit = box.set()

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    paint(comp, x, y)
    box.draw(comp, x + leftPadding, y)

  override def toString: String = s"FrameBox(height = $height, baselineHeight = $baselineHeight)"
