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
      // todo: check for vertical space at the end of a page
      println((page.pointLength + box.height, comp.pageHeight))
      book.add(page)
      page = newPage
      // todo: whether vertical space is being added at the beginning of a page
      page add box
    else page add box

  def result: PageBox = page

  def start: ParagraphMode =
    val paragraphMode = new ParagraphMode(comp, this)

    comp.modeStack push paragraphMode

    if comp.indentParagraph && !firstParagraph then paragraphMode add new HSpaceBox(0, comp.parindent, 0)
    else firstParagraph = false

    paragraphMode
