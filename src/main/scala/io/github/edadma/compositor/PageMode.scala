package io.github.edadma.compositor

class PageMode(comp: Compositor, pageBox: PageBox) extends Mode:
  protected var firstParagraph: Boolean = true

  def add(box: Box): Unit = pageBox add box

  def done(): Unit = {}
