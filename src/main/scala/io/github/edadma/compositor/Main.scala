package io.github.edadma.compositor

import io.github.edadma.libcairo.pdfSurfaceCreate

@main def run(): Unit =
  val pdf = Compositor.pdf("test.pdf", 612, 792)

  pdf += "[]|Testing 1 - 2 - 3"
  pdf.paragraph(500)
  pdf += "asdf"
  pdf.paragraph(500)
  pdf.draw()
  pdf.destroy()

//  val surface = pdfSurfaceCreate("test.pdf", 612, 792)
//  val context = surface.create
