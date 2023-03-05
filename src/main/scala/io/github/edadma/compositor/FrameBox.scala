package io.github.edadma.compositor

class FrameBox(box: Box) extends Box:
//  private var _width: Double = box.width
//  private var _height: Double = box.height

  var background: Color | Null = null
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
    if background ne null then
      comp.color(background)
      comp.ctx.rectangle(
        x,
        y - (if tight then ascender else ascent),
        width,
        if tight then ascender + descender else height,
      )
      comp.ctx.fill()

  def width: Double = box.width + leftPadding + rightPadding // _width

  def height: Double = box.height /*_height*/ + topPadding + bottomPadding

  def ascender: Double = box.ascender + topPadding

  def ascent: Double = box.ascent + topPadding

  def descent: Double = box.descent + bottomPadding

  def descender: Double = box.descender + bottomPadding

  def setToWidth(width: Double): Unit =
    box.setToWidth(width)
//    _width = box.width
//    _height = box.height

  def setToHeight(height: Double): Unit =
    box.setToHeight(height)
//    _width = box.width
//    _height = box.height

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    paint(comp, x, y)
    box.draw(comp, x + leftPadding, y)