package io.github.edadma.compositor

import io.github.edadma.libcairo.{Context, Surface, TextExtents, pdfSurfaceCreate}

import scala.collection.mutable.ArrayBuffer

class Compositor:
  val surfaces = new ArrayBuffer[Surface]
  val contexts = new ArrayBuffer[Context]

  def pdf(path: String, width: Double, height: Double): Context =
    val surface = pdfSurfaceCreate(path, width, height)

    surfaces += surface

    val context = surface.create

    contexts += context
    context
