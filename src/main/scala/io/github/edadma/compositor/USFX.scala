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
          case ("h", _)    => processBody()
          case ("toc", "level" -> "1") =>
            comp.bold()
            processBody()
            comp.nobold()
            comp.paragraph()
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
