package io.github.edadma.compositor

import io.github.edadma.xml.{XML, Element, Text}

object USFX:
  var verse: Option[String] = None

  def fromXML(comp: Compositor, node: XML): Unit =
    node match
      case Element("id" | "ide" | "ve" | "f" | "ft" | "fr" | "x", attrs, body) =>
      case Element(label, attrs, body) =>
        def processBody(): Unit = body foreach (c => fromXML(comp, c))

        (label, attrs.headOption.orNull) match
          case ("book", _) => processBody()
          case ("h", _)    =>
          case ("toc", "level" -> "1") =>
            val f = comp.currentFont

            comp.selectFont("noto", 18, "sans")
            comp.hbox()
            comp.hfil()
            processBody()
            comp.hfil()
            comp.done()
            comp.nobold()
            comp.selectFont(f)
            comp.vspace(25, 1)
          case ("toc", _) =>
          case ("c", "id" -> n) =>
            val f = comp.currentFont

            comp.selectFont("noto", 16, "sans")
            comp.hbox()
            comp.addText(s"Chapter $n")
            comp.hfil()
            comp.done()
            comp.selectFont(f)
            comp.vspace(10, 0.1)
          case ("wj", _)            => processBody()
          case ("p", "sfm" -> "mt") =>
          case ("p", "style" -> "p") =>
            comp.start()
            processBody()
            comp.paragraph()
          case ("p", _) =>
          case ("w", _) => processBody()
          case ("v", _) => verse = Some(attrs.find({ case (k, _) => k == "id" }).get._2)
          case _        => sys.error(s"don't know what to do with element <$label> with attributes $attrs")
      case Text(text) =>
        if !text.isBlank then
          if verse.isDefined then
            comp.prefixSup(verse.get, text)
            verse = None
          else comp.addText(text.trim)
      case _ => sys.error(s"don't know what to do with '$node' (${node.getClass})")

  def fromString(comp: Compositor, s: String): Unit = fromXML(comp, XML(scala.io.Source.fromString(s)))

  def fromFile(comp: Compositor, file: String): Unit = fromXML(comp, XML(scala.io.Source.fromFile(file)))
