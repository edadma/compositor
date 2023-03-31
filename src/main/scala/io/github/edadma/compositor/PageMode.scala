package io.github.edadma.compositor

class PageMode(protected val comp: Compositor, val result: PageBox) extends Mode:
  protected[compositor] var firstParagraph: Boolean = true

  def add(box: Box): Unit =
    if box.typ == Type.Start then start add box
    else result add box

  def start: ParagraphMode =
    val paragraphMode = new ParagraphMode(comp, this)

    comp.modeStack push paragraphMode

    if comp.indentParagraph && !firstParagraph then paragraphMode add new RigidBox(width = comp.parindent)
    else firstParagraph = false

    paragraphMode
