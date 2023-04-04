package io.github.edadma.compositor

import io.github.edadma.libcairo.imageSurfaceCreateFromPNG

class ImageBox(comp: Compositor, path: String, width: Option[Double] = None, height: Option[Double] = None)
    extends SimpleBox:
  val image = imageSurfaceCreateFromPNG(path)
  val imageWidth = image.getWidth
  val imageHeight = image.getHeight

  def ascent: Double = ???
  def draw(comp: io.github.edadma.compositor.Compositor, x: Double, y: Double): Unit = ???
  def typ: io.github.edadma.compositor.Type = ???
  def width: Double = ???
