package io.github.edadma.compositor

class PageMode(comp: Compositor, pageBox: PageBox) extends Mode:
  def add(box: Box): Unit = pageBox add box

  def done(): Unit = {}
