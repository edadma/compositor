package io.github.edadma.compositor

import io.github.edadma.libcairo.pdfSurfaceCreate

@main def run(): Unit =
  val doc = Compositor.png("test.png", 1280, 720, ppi(1920, 1080, 13), simplePage(Some(Color(0x19, 0x25, 0x40))))
  //  val doc = Compositor.pdf("test.pdf", 4, 5, lowerThirdPage)

  doc.loadFont("gentium", "GalSIL21/GalSILR.ttf" /*"GentiumPlus-6.200/GentiumPlus-Regular.ttf"*/ )
  doc.font("gentium", 14)
  doc.color("white")
  doc addText "As I walked through the wilderness of this world, I lighted on a certain place where was a Den, and I laid me down in that place to sleep: and, as I slept, I dreamed a dream. I dreamed, and behold, I saw a man clothed with rags, standing in a certain place, with his face from his own house, a book in his hand, and a great burden upon his back. [Isa. 64:6; Luke 14:33; Ps. 38:4; Hab. 2:2; Acts 16:30,31] I looked, and saw him open the book, and read therein; and, as he read, he wept, and trembled; and, not being able longer to contain, he brake out with a lamentable cry, saying, ``What shall I do?'' [Acts 2:37]"
  doc.paragraph()
//  doc add new VSpaceBox(1)
//  doc.size(30)
//  doc.center("``Message Title''")
//  doc add new VSpaceBox(0, 10, 0)
//  doc.size(20)
//  doc.center("Speaker's Name")
//  doc add new VSpaceBox(1)
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
