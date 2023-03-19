package io.github.edadma.compositor

class PageMode(protected val comp: Compositor, protected[compositor] val page: PageBox) extends Mode:
  protected[compositor] var firstParagraph: Boolean = true

  def add(box: Box): Unit =
    if box.isHorizontal then
      val paragraphMode = new ParagraphMode(comp, this)

      comp.modeStack push paragraphMode
      paragraphMode.add(box)
    else page add box

  def done(): Unit = {}
