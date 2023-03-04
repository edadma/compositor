package io.github.edadma.compositor

import io.github.edadma.libcairo.pdfSurfaceCreate

@main def run(): Unit =
  val doc = Compositor.png("test.png", 1280, 720, ppi(1920, 1080, 13), simplePage(Some(Color(0, .2, .8))))
  //  val doc = Compositor.pdf("test.pdf", 4, 5, lowerThirdPage)

  doc.loadFont("gentium", "GentiumPlus-6.200/GentiumPlus-Regular.ttf")
  doc.font("gentium", 12)
  doc.color(1, 1, 1)
  doc add new VSpaceBox(1)
  doc.size(30)
  doc.center("``Message Title''")
  doc add new VSpaceBox(0, 10, 0)
  doc.size(20)
  doc.center("Speaker's Name")
  doc add new VSpaceBox(1)
  doc.draw()
  doc.destroy()

//  val doc = Compositor.png("test.png", 1280, 720, ppi(1920, 1080, 13), lowerThirdPage)
////  val doc = Compositor.pdf("test.pdf", 4, 5, lowerThirdPage)
//
//  doc.color(1, 1, 1)
//  doc.bold()
//  doc.line(doc.textBox("Galatians 5:22-23"))
//  doc.normal()
//  doc add new VSpaceBox(0, 10, 0)
//  doc.noindent()
//  doc sup "22"
//  doc addText "But the fruit of the Spirit is love, joy, peace, forbearance, kindness, goodness, faithfulness,"
//  doc sup "23"
//  doc addText "gentleness and self-control. Against such things there is no law."
//  doc.paragraph()
//  doc.draw()
//  doc.destroy()
