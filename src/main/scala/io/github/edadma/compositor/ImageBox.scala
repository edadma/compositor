package io.github.edadma.compositor

import io.github.edadma.libcairo.imageSurfaceCreateFromPNG

class ImageBox(comp: Compositor, path: String, scaledWidth: Option[Double] = None, scaledHeight: Option[Double] = None)
    extends SimpleBox:
  val image = imageSurfaceCreateFromPNG(path)
  val imageWidth = image.getWidth
  val imageHeight = image.getHeight

  def draw(comp: io.github.edadma.compositor.Compositor, x: Double, y: Double): Unit = ???
  def typ: io.github.edadma.compositor.Type = Type.Horizontal
  def width: Double = scaledWidth getOrElse pixelsToPoints(imageWidth, ppi(1280, 720, 14))
  def ascent: Double = scaledHeight getOrElse pixelsToPoints(imageHeight, ppi(1280, 720, 14))
