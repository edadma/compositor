package io.github.edadma.compositor

class HorizontalMode(protected val comp: Compositor) extends Mode:
  protected[compositor] val hbox: HBox = new HBox

  def add(box: Box): Unit = hbox add box

  def done(): Unit =
    pop
    top add hbox
