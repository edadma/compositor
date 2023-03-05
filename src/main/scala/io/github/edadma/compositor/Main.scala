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
  verses(
    doc,
    "18 Now the birth of Jesus Christ[e] took place in this way. When his mother Mary had been betrothed[f] to Joseph, before they came together she was found to be with child from the Holy Spirit. 19 And her husband Joseph, being a just man and unwilling to put her to shame, resolved to divorce her quietly. 20 But as he considered these things, behold, an angel of the Lord appeared to him in a dream, saying, “Joseph, son of David, do not fear to take Mary as your wife, for that which is conceived in her is from the Holy Spirit. 21 She will bear a son, and you shall call his name Jesus, for he will save his people from their sins.” 22 All this took place to fulfill what the Lord had spoken by the prophet:\n\n23 “Behold, the virgin shall conceive and bear a son,\n    and they shall call his name Immanuel”\n\n(which means, God with us). 24 When Joseph woke from sleep, he did as the angel of the Lord commanded him: he took his wife, 25 but knew her not until she had given birth to a son. And he called his name Jesus.",
  )
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
