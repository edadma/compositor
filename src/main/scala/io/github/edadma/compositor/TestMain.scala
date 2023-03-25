package io.github.edadma.compositor

@main def runTest(): Unit =
  val doc = Compositor.png("test.png", 1280, 720, ppi(1280, 720, 14), simplePageFactory())

  doc.modeStack.push(new PageMode(doc, new SimplePage(300, Some(200))))
  doc.addText(
    "As I walked through the wilderness of this world, I lighted on a certain place where was a Den, and I laid me down in that place to sleep: and, as I slept, I dreamed a dream.",
  )
  doc.done()
  doc.addText(
    "In this plight, therefore, he went home and refrained himself as long as he could, that his wife and children should not perceive his distress; but he could not be silent long, because that his trouble increased. Wherefore at length he brake his mind to his wife and children; and thus he began to talk to them: O my dear wife, said he, and you the children of my bowels, I, your dear friend, am in myself undone by reason of a burden that lieth hard upon me; moreover, I am for certain informed that this our city will be burned with fire from heaven; in which fearful overthrow, both myself, with thee my wife, and you my sweet babes, shall miserably come to ruin, except (the which yet I see not) some way of escape can be found, whereby we may be delivered. At this his relations were sore amazed; not for that they believed that what he had said to them was true, but because they thought that some frenzy distemper had got into his head; therefore, it drawing towards night, and they hoping that sleep might settle his brains, with all haste they got him to bed. But the night was as troublesome to him as the day; wherefore, instead of sleeping, he spent it in sighs and tears. So, when the morning was come, they would know how he did. He told them, Worse and worse: he also set to talking to them again; but they began to be hardened. They also thought to drive away his distemper by harsh and surly carriages to him; sometimes they would deride, sometimes they would chide, and sometimes they would quite neglect him. Wherefore he began to retire himself to his chamber, to pray for and pity them, and also to condole his own misery; he would also walk solitarily in the fields, sometimes reading, and sometimes praying: and thus for some days he spent his time.",
  )
  doc.output()
  doc.destroy()