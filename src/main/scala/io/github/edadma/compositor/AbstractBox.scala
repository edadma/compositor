package io.github.edadma.compositor

abstract class AbstractBox extends Box:
  var _width: Option[Double] = None
  var _height: Option[Double] = None

  def setToWidth(width: Double): Unit = _width = Some(width)

  def setToHeight(height: Double): Unit = _height = Some(height)

  def naturalWidth: Double

  def naturalAscent: Double

  def naturalDescent: Double

  def width: Double = _width getOrElse naturalWidth

  def height: Double = _height getOrElse (naturalHeight + naturalDescent)
