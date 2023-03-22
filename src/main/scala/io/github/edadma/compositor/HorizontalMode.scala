package io.github.edadma.compositor

abstract class HorizontalMode extends Mode:
  protected[compositor] val hbox: HBox = new HBox

  protected def addBox(box: Box): Unit

  protected def nonEmpty: Boolean

  protected def last: Box

  def add(box: Box): Unit =
    if nonEmpty then
      val space =
        last match
          case b: CharBox
              if b.text.nonEmpty &&
                !(b.text.last == '.' && Abbreviation(b.text.dropRight(1))) &&
                ".!?:;".contains(b.text.last) =>
            comp.currentFont.space * 1.5
          case _ => comp.currentFont.space

      addBox(
        new HSpaceBox(
          0,
          space,
        ),
      )

    addBox(box)
  end add

  def done(): Unit =
    pop
    top add hbox
