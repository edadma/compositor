package io.github.edadma.compositor

import scala.xml.{XML, Node, Elem, Text}

object USFX:
  def fromXML(comp: Compositor, node: Node): Unit =
    node match
      case Elem(prefix, "id" | "ide" | "ve" | "f" | "ft" | "fr", attribs, scope, child @ _*) =>
      case Elem(prefix, label, attribs, scope, child @ _*) =>
        def body(): Unit = child foreach (c => fromXML(comp, c))

        (label, attribs.asAttrMap.headOption) match
          case ("book", _) => body()
          case ("h", _)    => body()
          case ("toc", "level" -> "1") =>
            comp.bold()
            body()
            comp.nobold()
          case ("toc", _)            =>
          case ("c", _)              =>
          case ("wj", _)             => body()
          case ("p", "sfm" -> "mt")  =>
          case ("p", "style" -> "p") => body()
          case _ => sys.error(s"don't know what to do with element <$label> with attributes $attribs")
      case Text(text) => if !text.isBlank then comp.add(text.trim)
      case _          => sys.error(s"don't know what to do with '$node' (${node.getClass})")

  def fromString(comp: Compositor, s: String): Unit = fromXML(comp, XML.loadString(s))

  def fromFile(comp: Compositor, file: String): Unit = fromXML(comp, XML.loadFile(file))
