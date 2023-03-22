package io.github.edadma.compositor

class PageMode(protected val comp: Compositor, val result: PageBox) extends Mode:
  protected[compositor] var firstParagraph: Boolean = true

  def add(box: Box): Unit =
    if box.isHorizontal then
      val paragraphMode = new ParagraphMode(comp, this)

      comp.modeStack push paragraphMode

      if comp.indent && !firstParagraph then paragraphMode add new RigidBox(width = comp.parindent)

      paragraphMode add box
    else result add box
