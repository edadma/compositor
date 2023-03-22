package io.github.edadma.compositor

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

class ParagraphMode(protected val comp: Compositor, pageMode: PageMode) extends HorizontalMode:
  val boxes = new ArrayBuffer[Box]

  protected def addBox(box: Box): Unit = boxes += box

  protected def nonEmpty: Boolean = boxes.nonEmpty

  protected def last: Box = boxes.last

  def done(): Unit =
    while boxes.nonEmpty do
      val hbox = new HBox

      @tailrec
      def line(): Unit =
        if boxes.nonEmpty then
          if hbox.width + boxes.head.width <= pageMode.page.lineWidth then
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

                            if hbox.width + beforeHyphen.width <= pageMode.page.lineWidth then
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

                    if hbox.width + beforeHyphen.width <= pageMode.page.lineWidth then
                      hbox add beforeHyphen
                      boxes.remove(0)
                      boxes.insert(0, b.newCharBox(b.text.substring(idx + 1)))
              case _ =>
            end match

      line()

      if hbox.boxes.last.isSpace then hbox.boxes.remove(hbox.boxes.length - 1)
      if boxes.nonEmpty && boxes.head.isSpace then boxes.remove(0)
      if boxes.isEmpty then hbox add new HSpaceBox(2)

      pageMode.page add hbox
    end while

    comp.indent = true
    pageMode.firstParagraph = false
    pop
  end done
