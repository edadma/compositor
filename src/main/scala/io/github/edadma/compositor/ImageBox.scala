package io.github.edadma.compositor

import io.github.edadma.libcairo.imageSurfaceCreateFromPNG

class ImageBox(comp: Compositor, path: String, scaledWidth: Option[Double] = None, scaledHeight: Option[Double] = None)
    extends SimpleBox:
  val image = imageSurfaceCreateFromPNG(path)
  val imageWidth = image.getWidth
  val imageHeight = image.getHeight

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    comp.ctx.save()
//    comp.ctx.identityMatrix()
    // todo: each box should set it's own source, and then save()/restore() don't have to be used
    comp.ctx.setSourceSurface(image, x, y - ascent)
    comp.ctx.paint()
    comp.ctx.restore()
  // image.destroy()

  val typ: Type = Type.Horizontal

  val width: Double = scaledWidth getOrElse imageWidth

  val ascent: Double = scaledHeight getOrElse imageHeight
