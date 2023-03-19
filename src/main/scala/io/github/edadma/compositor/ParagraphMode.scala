package io.github.edadma.compositor

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

class ParagraphMode(comp: Compositor, page: PageBox) extends Mode:
  var firstParagraph = true
  var indent = true
  val boxes = new ArrayBuffer[Box]

  def add(box: Box): Unit =
    if boxes.nonEmpty then
      val space =
        boxes.last match
          case b: CharBox
              if b.text.nonEmpty &&
                !(b.text.last == '.' && Abbreviation(b.text.dropRight(1))) &&
                ".!?:;".contains(b.text.last) =>
            comp.currentFont.space * 1.5
          case _ => comp.currentFont.space

      boxes += new HSpaceBox(
        0,
        space,
      )
    else if indent && !firstParagraph then boxes += new RigidBox(width = parindent)

    boxes += box
  end add

  def done(): Unit = {}

  def paragraph(): Unit =
    while boxes.nonEmpty do
      val hbox = new HBox

      @tailrec
      def line(): Unit =
        if boxes.nonEmpty then
          if hbox.width + boxes.head.width <= page.lineWidth then
            hbox add boxes.remove(0)
            line()
          else
            boxes.head match
              case b: CharBox =>
                b.text indexOf '-' match
                  case -1 =>
                    Hyphenation(b.text) match
                      case None =>
                      case Some(hyphenation) =>
                        var lastBefore: CharBox = null
                        var lastAfter: String = null

                        @tailrec
                        def longest(): Unit =
                          if hyphenation.hasNext then
                            val (before, after) = hyphenation.next
                            val beforeHyphen = b.newCharBox(before)

                            if hbox.width + beforeHyphen.width <= page.lineWidth then
                              lastBefore = beforeHyphen
                              lastAfter = after
                              longest()

                        longest()

                        if lastBefore ne null then
                          hbox add lastBefore
                          boxes.remove(0)
                          boxes.insert(0, b.newCharBox(lastAfter))
                    end match
                  case idx =>
                    val beforeHyphen = b.newCharBox(b.text.substring(0, idx + 1))

                    if hbox.width + beforeHyphen.width <= page.lineWidth then
                      hbox add beforeHyphen
                      boxes.remove(0)
                      boxes.insert(0, b.newCharBox(b.text.substring(idx + 1)))
              case _ =>
            end match

      line()

      if hbox.boxes.last.isSpace then hbox.boxes.remove(hbox.boxes.length - 1)
      if boxes.nonEmpty && boxes.head.isSpace then boxes.remove(0)
      if boxes.isEmpty then hbox add new HSpaceBox(2)

      comp.modeStack.top add hbox
    end while

    indent = true
    firstParagraph = false
  end paragraph
