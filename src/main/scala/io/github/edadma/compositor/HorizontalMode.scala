package io.github.edadma.compositor

abstract class HorizontalMode extends Mode:
  protected def addBox(box: Box): Unit

  protected def nonEmpty: Boolean

  protected def last: Box

  def add(box: Box): Unit =
    if nonEmpty then
      last match
        case b: CharBox
            if b.text.nonEmpty &&
              !(b.text.last == '.' && Abbreviation(b.text.dropRight(1))) &&
              ".!?:;".contains(b.text.last) =>
          addBox(new HSpaceBox(0, comp.currentFont.space * 1.5))
        case _: CharBox => addBox(new HSpaceBox(0, comp.currentFont.space))
        case _          =>

    addBox(box)
  end add
