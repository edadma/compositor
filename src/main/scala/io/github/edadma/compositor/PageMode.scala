package io.github.edadma.compositor

class PageMode(protected val comp: Compositor, protected[compositor] val page: PageBox) extends Mode:
  protected var firstParagraph: Boolean = true

  def add(box: Box): Unit = page add box

  def done(): Unit = {}
