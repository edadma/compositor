package io.github.edadma.compositor

class HBoxMode(protected val comp: Compositor, val hbox: HBox) extends HorizontalMode:
  protected def addBox(box: Box): Unit = hbox add box

  protected def nonEmpty: Boolean = hbox.nonEmpty

  protected def last: Box = hbox.last

  def done(): Unit =
    pop
    top add hbox
