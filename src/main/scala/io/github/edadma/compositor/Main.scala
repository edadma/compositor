package io.github.edadma.compositor

import io.github.edadma.libcairo.pdfSurfaceCreate

@main def run(): Unit =
  val pdf = Compositor.pdf("test.pdf", 612, 792)

  pdf add new VSpaceBox(1)
  pdf addBox new HSpaceBox(1)
  pdf addText "Pilgrim's Progress"
  pdf.paragraph(165)
  pdf add new VSpaceBox(0, 20, 0)
  pdf addBox pdf.sup("10")
  pdf addText "As I walked through the wilderness of this world"
  pdf.paragraph(165)
  pdf.size(pdf.currentFont.size * .5)
  pdf addText "As I walked through the wilderness of this world, I lighted on a certain place where was a Den, and I laid me down in that place to sleep: and, as I slept, I dreamed a dream."
  pdf.paragraph(165)
  pdf.size(10)
  pdf addText "As I walked through the wilderness of this world"
  pdf.paragraph(165)
  pdf add new VSpaceBox(1)
  pdf.draw()
  pdf.destroy()

/*
pdf addText "As I walked through the wilderness of this world, I lighted on a certain place where was a Den, and I laid me down in that place to sleep: and, as I slept, I dreamed a dream. I dreamed, and behold, I saw a man clothed with rags, standing in a certain place, with his face from his own house, a book in his hand, and a great burden upon his back. [Isa. 64:6; Luke 14:33; Ps. 38:4; Hab. 2:2; Acts 16:30,31] I looked, and saw him open the book, and read therein; and, as he read, he wept, and trembled; and, not being able longer to contain, he brake out with a lamentable cry, saying, \"What shall I do?\" [Acts 2:37] "
pdf.paragraph(500)
pdf addText "In this plight, therefore, he went home and refrained himself as long as he could, that his wife and children should not perceive his distress; but he could not be silent long, because that his trouble increased. Wherefore at length he brake his mind to his wife and children; and thus he began to talk to them: O my dear wife, said he, and you the children of my bowels, I, your dear friend, am in myself undone by reason of a burden that lieth hard upon me; moreover, I am for certain informed that this our city will be burned with fire from heaven; in which fearful overthrow, both myself, with thee my wife, and you my sweet babes, shall miserably come to ruin, except (the which yet I see not) some way of escape can be found, whereby we may be delivered. At this his relations were sore amazed; not for that they believed that what he had said to them was true, but because they thought that some frenzy distemper had got into his head; therefore, it drawing towards night, and they hoping that sleep might settle his brains, with all haste they got him to bed. But the night was as troublesome to him as the day; wherefore, instead of sleeping, he spent it in sighs and tears. So, when the morning was come, they would know how he did. He told them, Worse and worse: he also set to talking to them again; but they began to be hardened. They also thought to drive away his distemper by harsh and surly carriages to him; sometimes they would deride, sometimes they would chide, and sometimes they would quite neglect him. Wherefore he began to retire himself to his chamber, to pray for and pity them, and also to condole his own misery; he would also walk solitarily in the fields, sometimes reading, and sometimes praying: and thus for some days he spent his time. "
pdf.paragraph(500)
 */
