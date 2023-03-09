package io.github.edadma.compositor

class FrameBox(box: Box) extends Box:
  var rounded: Boolean = true
  var background: Color | Null = null
  var border: Color | Null = null
  var borderWidth: Double = 0.5
  var cornerRadius: Double = 3
  var topPadding: Double = 0
  var bottomPadding: Double = 0
  var leftPadding: Double = 0
  var rightPadding: Double = 0
  var tight: Boolean = false

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
          y - (if tight then ascender else ascent),
          width,
          if tight then ascender + descender else height,
          cornerRadius,
        )
      else
        comp.ctx.rectangle(
          x,
          y - (if tight then ascender else ascent),
          width,
          if tight then ascender + descender else height,
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

  def width: Double = box.width + leftPadding + rightPadding

  def height: Double = box.height + topPadding + bottomPadding

  def ascender: Double = box.ascender + topPadding

  def ascent: Double = box.ascent + topPadding

  def descent: Double = box.descent + bottomPadding

  def descender: Double = box.descender + bottomPadding

  def setToWidth(width: Double): Unit = box.setToWidth(width)

  def setToHeight(height: Double): Unit = box.setToHeight(height)

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    paint(comp, x, y)
    box.draw(comp, x + leftPadding, y)
