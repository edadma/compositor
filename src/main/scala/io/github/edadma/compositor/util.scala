package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

private[compositor] object util:
  def roundedRectanglePath(
      ctx: Context,
      x: Double,
      y: Double,
      width: Double,
      height: Double,
  ): Unit =
    val aspect = 1.0
    val corner_radius = height / 10.0
    val radius = corner_radius / aspect
    val degrees = math.Pi / 180.0

    ctx.newSubPath()
    ctx.arc(x + width - radius, y + radius, radius, -90 * degrees, 0 * degrees)
    ctx.arc(x + width - radius, y + height - radius, radius, 0 * degrees, 90 * degrees)
    ctx.arc(x + radius, y + height - radius, radius, 90 * degrees, 180 * degrees)
    ctx.arc(x + radius, y + radius, radius, 180 * degrees, 270 * degrees)
    ctx.closePath()
  end roundedRectanglePath
