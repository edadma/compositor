package io.github.edadma.compositor

import io.github.edadma.libcairo.pdfSurfaceCreate

@main def run(): Unit =
//  val doc = Compositor.png("test.png", 1280, 720, ppi(1920, 1080, 13), simplePage(Some(Color(0x19, 0x25, 0x40, 255))))
  val doc = Compositor.pdf(
    "test.pdf",
    8.5,
    11,
    simplePageFactory(),
  )

  doc.loadFont("galatia", "GalSIL21/GalSILR.ttf")
  doc.loadFont("galatia", "GalSIL21/GalSILB.ttf", "bold")
  doc.loadFont("narrow", "PT_Sans_Narrow/PTSansNarrow-Regular.ttf")
  doc.loadFont("narrow", "PT_Sans_Narrow/PTSansNarrow-Bold.ttf", "bold")
  doc.loadFont("pragati", "PragatiNarrow/PragatiNarrow-Regular.ttf")
  doc.loadFont("pragati", "PragatiNarrow/PragatiNarrow-Bold.ttf", "bold")

  doc.selectFont("galatia", 12)
  doc.color("black")
  doc.prefixSup("12", "asdf")
  doc addText "As I walked through the"
  doc.bold()
  doc addBox new FrameBox(doc.textBox("wilderness")) { background = Color("orange"); padding(5) }
  doc.nobold()
  doc addText "of this world, I lighted on a certain place where was a Den, and I laid me down in that place to sleep: and, as I slept, I dreamed a dream. I dreamed, and behold, I saw a man clothed with rags, standing in a certain place, with his face from his own house, a book in his hand, and a great burden upon his back. [Isa. 64:6; Luke 14:33; Ps. 38:4; Hab. 2:2; Acts 16:30,31] I looked, and saw him open the book, and read therein; and, as he read, he wept, and trembled; and, not being able longer to contain, he brake out with a lamentable cry, saying, ``What shall I do?'' [Acts 2:37]"
  doc.paragraph()
  doc addText "In this plight, therefore, he went home and refrained himself as long as he could, that his wife and children should not perceive his distress; but he could not be silent long, because that his trouble increased. Wherefore at length he brake his mind to his wife and children; and thus he began to talk to them: O my dear wife, said he, and you the children of my bowels, I, your dear friend, am in myself undone by reason of a burden that lieth hard upon me; moreover, I am for certain informed that this our city will be burned with fire from heaven; in which fearful overthrow, both myself, with thee my wife, and you my sweet babes, shall miserably come to ruin, except (the which yet I see not) some way of escape can be found, whereby we may be delivered. At this his relations were sore amazed; not for that they believed that what he had said to them was true, but because they thought that some frenzy distemper had got into his head; therefore, it drawing towards night, and they hoping that sleep might settle his brains, with all haste they got him to bed. But the night was as troublesome to him as the day; wherefore, instead of sleeping, he spent it in sighs and tears. So, when the morning was come, they would know how he did. He told them, Worse and worse: he also set to talking to them again; but they began to be hardened. They also thought to drive away his distemper by harsh and surly carriages to him; sometimes they would deride, sometimes they would chide, and sometimes they would quite neglect him. Wherefore he began to retire himself to his chamber, to pray for and pity them, and also to condole his own misery; he would also walk solitarily in the fields, sometimes reading, and sometimes praying: and thus for some days he spent his time."
  doc.paragraph()

//  doc add new VSpaceBox(1)
//  doc.size(30)
//  doc.center("``Message Title''")
//  doc add new VSpaceBox(0, 10, 0)
//  doc.size(20)
//  doc.center("Speaker's Name")
//  doc add new VSpaceBox(1)
  doc.set()
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
