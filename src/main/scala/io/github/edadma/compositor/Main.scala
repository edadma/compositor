package io.github.edadma.compositor

import io.github.edadma.libcairo.pdfSurfaceCreate

@main def run(): Unit =
  val doc = Compositor.png("test.png", 1280, 720, ppi(1920, 1080, 13), simplePageFactory())

//  val doc = Compositor.pdf(
//    "test.pdf",
//    Paper.LETTER,
//    simplePageFactory(),
//  )

//  doc add new FrameBox(new RigidBox(doc.pageWidth, doc.pageHeight * 2 / 3)) { background = Color.TRANSPARENT }
//  doc.startPage(new SimplePage(doc.pageWidth - 10, Some(doc.pageHeight * 3 - 5)))
//  verses(
//    doc,
//    "20 But as he considered these things, behold, an angel of the Lord appeared to him in a dream, saying, “Joseph, son of David, do not fear to take Mary as your wife, for that which is conceived in her is from the Holy Spirit. 21 She will bear a son, and you shall call his name Jesus, for he will save his people from their sins.” 22 All this took place to fulfill what the Lord had spoken by the prophet:\n\n23 “Behold, the virgin shall conceive and bear a son,\n    and they shall call his name Immanuel”\n\n(which means, God with us).",
//  )
//  doc addText "Koine Greek (UK: /ˈkɔɪniː/ COY-nee[2] US: /ˈkɔɪneɪ/ COY-nay or /kɔɪˈneɪ/ coy-NAY[3][4]; Koine Greek: ἡ κοινὴ διάλεκτος, romanized: hē koinè diálektos, lit. 'the common dialect'; Greek: [i cyˈni ðiˈalektos])"
//  doc.paragraph()
//  doc.selectFont("notosans", 12)
  doc addText "office file waffle flux"
  doc.paragraph()
  doc setStyle "bold"
  doc.noindent()
  doc addBox new UnderlineBox(doc, (new HBox).add(doc.textBox("office")))
  doc addBox new FrameBox(doc.textBox("file")) {
    border = Color("green")
//    background = Color("teal")
  }
  doc addText "waffle flux"
  doc.paragraph()
  doc setStyle "italic"
  doc.noindent()
  doc addBox new UnderlineBox(doc, (new HBox).add(doc.textBox("office")))
  doc addText "file waffle flux"
  doc.paragraph()
  doc.setStyle("bold", "italic")
  doc.noindent()
  doc addText "office file waffle flux"
  doc.paragraph()

  //  doc add new VSpaceBox(1)
//  doc add new FrameBox(doc.endPage) {
//    background = Color("black", 0.5)
//    leftPadding = 5
//    rightPadding = 5
//    topPadding = 5
//  }
  doc.draw()
  doc.destroy()
