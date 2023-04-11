package io.github.edadma.compositor

import io.github.edadma.xml.{XML, Element, Text}

object USFX:
  def fromXML(comp: Compositor, node: XML): Unit =
    node match
      case Element(pos, "id" | "ide" | "ve" | "f" | "ft" | "fr", attrs, body) =>
      case Element(pos, label, attrs, body) =>
        def processBody(): Unit = body foreach (c => fromXML(comp, c))

        (label, attrs.headOption.orNull) match
          case ("book", _) => processBody()
          case ("h", _) =>
            val f = comp.currentFont

            comp.selectFont("noto", 20)
            comp.hbox()
            comp.hfil()
            processBody()
            comp.hfil()
            comp.done()
            comp.selectFont(f)
            comp.vspace(10)
          case ("toc", "level" -> "1") =>
            val f = comp.currentFont

            comp.selectFont("noto", 16)
            comp.hbox()
            comp.hfil()
            processBody()
            comp.hfil()
            comp.done()
            comp.nobold()
            comp.selectFont(f)
            comp.vspace(15)
          case ("toc", _)           =>
          case ("c", _)             =>
          case ("wj", _)            => processBody()
          case ("p", "sfm" -> "mt") =>
          case ("p", "style" -> "p") =>
            processBody()
            comp.paragraph()
          case ("w", _) => processBody()
          case ("v", _) =>
          case _        => sys.error(s"don't know what to do with element <$label> with attributes $attrs")
      case Text(pos, text) => if !text.isBlank then comp.add(text.trim)
      case _               => sys.error(s"don't know what to do with '$node' (${node.getClass})")

  def fromString(comp: Compositor, s: String): Unit = fromXML(comp, XML(scala.io.Source.fromString(s)))

  def fromFile(comp: Compositor, file: String): Unit = fromXML(comp, XML(scala.io.Source.fromFile(file)))
