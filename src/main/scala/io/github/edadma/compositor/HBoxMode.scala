package io.github.edadma.compositor

class HBoxMode(protected val comp: Compositor) extends HorizontalMode:
  val result: HBox = new HBox

  protected def addBox(box: Box): Unit = result add box

  protected def nonEmpty: Boolean = result.nonEmpty

  protected def last: Box = result.last

  protected def length: Int = result.length

  protected def update(index: Int, elem: Box): Unit = result.update(index, elem)
