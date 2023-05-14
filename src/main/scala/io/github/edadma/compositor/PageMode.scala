package io.github.edadma.compositor

import pprint.pprintln

class PageMode(protected val comp: Compositor, book: Mode, val pageFactory: PageFactory) extends Mode:
  protected[compositor] var firstParagraph: Boolean = true

  def newPage: PageBox = pageFactory(comp, comp.pageWidth, comp.pageHeight)

  protected[compositor] var page: PageBox = newPage

  def add(box: Box): Unit =
    if box.typ == Type.Start then start add box
    else addLine(box)

  def addLine(box: Box): Unit =
    if page.pointLength + box.height > comp.pageHeight then
      page.lastOption foreach {
        case _: VSpaceBox => page.remove(page.length - 1)
        case _            =>
      }
      book.add(page)
      page = newPage
    end if

    if page.nonEmpty || !box.isInstanceOf[VSpaceBox] then page add box

  def result: PageBox = page

  def start: ParagraphMode =
    val paragraphMode = new ParagraphMode(comp, this)

    comp.modeStack push paragraphMode

    if comp.indentParagraph && !firstParagraph then paragraphMode add new HSpaceBox(0, comp.parindent, 0)
    else firstParagraph = false

    paragraphMode
