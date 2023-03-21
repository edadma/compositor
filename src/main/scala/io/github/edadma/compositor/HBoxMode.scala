package io.github.edadma.compositor

class HBoxMode(protected val comp: Compositor) extends HorizontalMode:
  protected[compositor] val hbox: HBox = new HBox

  def add(box: Box): Unit = hbox add box

  def done(): Unit =
    pop
    top add hbox
