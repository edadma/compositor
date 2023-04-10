package io.github.edadma.compositor

import io.github.edadma.char_reader.CharReader
import io.github.edadma.char_reader.CharReader.EOI

import pprint.pprintln

@main
def runxml(): Unit =
  pprintln(XML("""
      |<book id="JHN"><id id="JHN">43-JHN-web.sfm World English Bible (WEB)
      |</id><ide charset="UTF-8" /><h>John
      |</h><toc level="1">The Good News According to John
      |</toc><toc level="2">John
      |</toc><toc level="3">John
      |</toc><p sfm="mt" level="2" style="mt2">The Good News According to
      |</p><p sfm="mt" style="mt1">John
      |</p><c id="1" />
      |<p style="p"><v id="1" bcv="JHN.1.1" />
      |<w s="G1722">In</w>
      |<w s="G3588">the</w>
      |<w s="G0746">beginning</w>
      |<w s="G1510">was</w>
      |<w s="G3588">the</w>
      |<w s="G3056">Word</w>,
      |</p></book>
      |""".stripMargin))

object XML:
  private def skip(r: CharReader): CharReader = if r.ch.isWhitespace then skip(r.next) else r

//  private def skipUntil(r: CharReader): CharReader =
//    r.ch match
//      case EOI => r
//      case

  private def consume(
      r: CharReader,
      until: Char => Boolean,
      buf: StringBuilder = new StringBuilder,
  ): (String, CharReader) =
    r.ch match
      case EOI           => (buf.toString, r)
      case c if until(c) => (buf.toString, r)
      case c =>
        buf += c
        consume(r.next, until, buf)

  def parse(r: CharReader): (XML, CharReader) =
    val r1 = skip(r)

    r1.ch match
      case EOI => sys.error(s"unexpected end of input: $r")
      case '<' =>
        val (tag, r2) = consume(skip(r1.next), !_.isLetter)

      case _ =>
        val (text, r1) = consume(r, _ == '<')

        (Text(r, text), r1)

  def apply(s: scala.io.Source): XML = parse(CharReader.fromString(s.mkString))

abstract class XML:
  val pos: CharReader

case class Element(pos: CharReader, name: String, attrs: Seq[(String, String)], body: Seq[XML]) extends XML
case class Text(pos: CharReader, s: String) extends XML
