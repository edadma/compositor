package io.github.edadma.compositor

class HBoxMode(protected val comp: Compositor) extends HorizontalMode:
  val result: HBox = new HBox

  protected def addBox(box: Box): Unit = result add box

  protected def nonEmpty: Boolean = result.nonEmpty

  protected def last: Box = result.last
