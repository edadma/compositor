package io.github.edadma.compositor

abstract class Mode:
  protected val comp: Compositor

  def add(box: Box): Unit

  def result: Box

  def done(): Unit = comp.add(result)

  def pop: Mode = comp.modeStack.pop

  def top: Mode = comp.modeStack.top
