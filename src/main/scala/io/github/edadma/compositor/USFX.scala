package io.github.edadma.compositor

import scala.xml.{XML, Node, Elem, Text}

object USFX:
  def fromXML(comp: Compositor, node: Node): Unit =
    node match
      case Elem(prefix, label, attribs, scope, child @ _*) =>
        def body(): Unit = child foreach (c => fromXML(comp, c))

        (label, attribs.asAttrMap.headOption) match
          case ("toc", "level" -> "1") =>
            comp.bold()
            body()
            comp.nobold()
          case _ => sys.error(s"don't know what to do with element <$label> with attributes $attribs")

      case Text(text) => if !text.isBlank then comp.add(text)
      case _          => sys.error(s"don't know what to do with '$node' (${node.getClass})")

  def fromString(comp: Compositor, s: String): Unit = fromXML(comp, XML.loadString(s))

  def fromFile(comp: Compositor, file: String): Unit = fromXML(comp, XML.loadFile(file))
