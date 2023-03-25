package io.github.edadma.compositor

abstract class AbstractBox extends Box:
  protected var _width: Option[Double] = None
  protected var _height: Option[Double] = None

  def setToWidth(width: Double): Unit = _width = Some(width)

  def setToHeight(height: Double): Unit = _height = Some(height)

  def naturalWidth: Double

  def naturalAscent: Double

  def naturalDescent: Double

  def width: Double = _width getOrElse naturalWidth

  override def height: Double = _height getOrElse (naturalAscent + naturalDescent)
