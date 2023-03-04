package io.github.edadma.compositor

class BackgroundBox(box: Box) extends Box:
  private var _width: Double = box.width
  private var _height: Double = box.height

  var background: Color | Null = null

  def paint(comp: Compositor, x: Double, y: Double): Unit =
    if background ne null then
      comp.color(background)
      comp.ctx.rectangle(x, y, width, height)
      comp.ctx.fill()

  def width: Double = _width

  def height: Double = _height

  def ascender: Double = box.ascender

  def ascent: Double = box.ascent

  def descent: Double = box.descent

  def setToWidth(width: Double): Unit =
    box.setToWidth(width)
    _width = box.width
    _height = box.height

  def setToHeight(height: Double): Unit =
    box.setToHeight(_width)
    _width = box.width
    _height = box.height

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    paint(comp, x, y)
    box.draw(comp, x, y)
