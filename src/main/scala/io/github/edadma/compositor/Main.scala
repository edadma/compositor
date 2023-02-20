package io.github.edadma.compositor

import io.github.edadma.libcairo.pdfSurfaceCreate

@main def run(): Unit =
  val pdf = Compositor.pdf("test.pdf", 612, 792)

  pdf addText "Here is a collection of pointers to articles that have been written elsewhere about cairo. Many of these articles might provide good inpiration for generating new primary-source documentation. As we improve the primary documentation to cover the same material that is covered below, we can drop items from the following list, (or at least move the links to some other page)."
  pdf.paragraph(300)
  pdf.draw()
  pdf.destroy()

//  val surface = pdfSurfaceCreate("test.pdf", 612, 792)
//  val context = surface.create
