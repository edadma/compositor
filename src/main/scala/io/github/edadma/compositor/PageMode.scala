package io.github.edadma.compositor

import pprint.pprintln

class PageMode(protected val comp: Compositor, val pageFactory: PageFactory) extends Mode:
  protected[compositor] var firstParagraph: Boolean = true
  protected[compositor] var page: PageBox = pageFactory(comp, comp.pageWidth, comp.pageHeight)

  def add(box: Box): Unit =
    if box.typ == Type.Start then start add box
    else addLine(box)

  def addLine(box: Box): Unit = page add box

  def result: PageBox = page

  def start: ParagraphMode =
    val paragraphMode = new ParagraphMode(comp, this)

    comp.modeStack push paragraphMode

    if comp.indentParagraph && !firstParagraph then paragraphMode add new HSpaceBox(0, comp.parindent, 0)
    else firstParagraph = false

    paragraphMode
