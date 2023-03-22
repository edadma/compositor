package io.github.edadma.compositor

class HBoxMode(protected val comp: Compositor, val result: HBox) extends HorizontalMode:
  protected def addBox(box: Box): Unit = result add box

  protected def nonEmpty: Boolean = result.nonEmpty

  protected def last: Box = result.last
