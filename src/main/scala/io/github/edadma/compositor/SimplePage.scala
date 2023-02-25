package io.github.edadma.compositor

class SimplePage(val width: Double, val height: Double) extends PageBox:
  private val page = new VBox

  val lineWidth: Double = width

  def add(box: Box): Unit = page add box

  def set(): Unit =
    page.setToWidth(width)
    page.setToHeight(height)
