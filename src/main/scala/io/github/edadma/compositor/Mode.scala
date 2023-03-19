package io.github.edadma.compositor

abstract class Mode:
  protected val comp: Compositor

//  protected def material: List[Box]
  def add(box: Box): Unit

  def done(): Unit
