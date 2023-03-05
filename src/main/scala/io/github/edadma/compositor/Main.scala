package io.github.edadma.compositor

import io.github.edadma.libcairo.pdfSurfaceCreate

@main def run(): Unit =
//  val doc = Compositor.png("test.png", 1280, 720, ppi(1920, 1080, 13), simplePage(Some(Color(0x19, 0x25, 0x40, 255))))
  val doc = Compositor.pdf(
    "test.pdf",
    Paper.LETTER,
    simplePageFactory(),
  )

  doc.color("black")
  doc addText "“Behold, the virgin shall conceive and bear a son, and they shall call his name Immanuel”"
//  doc.startPage(new SimplePage(Units.in(4), Some(Units.in(2))))
//  doc addText "Now, I saw, upon a time, when he was walking in the fields, that he was, as he was wont, reading in his book, and greatly distressed in his mind; and, as he read, he burst out, as he had done before, crying, ``What shall I do to be saved?''"
//  doc.paragraph()
//  doc add new VSpaceBox(1)
//  doc add new FrameBox(doc.endPage) {
//    background = Color("lightgrey")
//    leftPadding = Units.in(2.25)
//    rightPadding = Units.in(2.25)
//    topPadding = 5
//  }
  doc.draw()
  doc.destroy()
