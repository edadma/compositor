package io.github.edadma.compositor

class HBoxMode(protected val comp: Compositor) extends HorizontalMode:
  protected[compositor] val hbox: HBox = new HBox

  protected def addBox(box: Box): Unit = hbox add box

  protected def nonEmpty: Boolean = hbox.nonEmpty

  protected def last: Box = hbox.last

  def done(): Unit =
    pop
    top add hbox
