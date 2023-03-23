package io.github.edadma.compositor

import io.github.edadma.char_reader.CharReader
import io.github.edadma.texish.{AST, Active, Command, Parser, Renderer, problem}
import pprint.pprintln

val commands =
  List(
    new Command("verses", 1):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(a: String) => verses(context.asInstanceOf[Compositor], a)
          case List(a)         => problem(pos, s"expected arguments <string>: $a")
          case _               => problem(pos, "expected arguments <string>")
    ,
    new Command("hbox", 1, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(a: AST) =>
            context.asInstanceOf[Compositor].hbox()
            renderer.render(a)
            context.asInstanceOf[Compositor].done()
          case List(a) => problem(pos, s"expected arguments <text>: $a")
          case _       => problem(pos, "expected arguments <text>")
    ,
    new Command("page", 2, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(w: AST, page: AST) =>
            val width = renderer.eval(w).asInstanceOf[Number].doubleValue()

            context.asInstanceOf[Compositor].page(width)
            renderer.render(page)
            context.asInstanceOf[Compositor].paragraph()
            context.asInstanceOf[Compositor].done()
          case _ => problem(pos, "expected arguments <width> <text>")
    ,
    new Command("bold", 1, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(a: AST) =>
            context.asInstanceOf[Compositor].bold()
            renderer.render(a)
            context.asInstanceOf[Compositor].nobold()
          case List(a) => problem(pos, s"expected arguments <text>: $a")
          case _       => problem(pos, "expected arguments <text>")
    ,
    new Command("italic", 1, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(a: AST) =>
            context.asInstanceOf[Compositor].italic()
            renderer.render(a)
            context.asInstanceOf[Compositor].noitalic()
          case List(a) => problem(pos, s"expected arguments <text>: $a")
          case _       => problem(pos, "expected arguments <text>")
    ,
    new Command("underline", 1, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(a: AST) =>
            val hbox = new HBox

            context.asInstanceOf[Compositor].hbox(hbox)
            renderer.render(a)
            context.asInstanceOf[Compositor].modeStack.pop()
            context.asInstanceOf[Compositor].add(new UnderlineBox(context.asInstanceOf[Compositor], hbox))
          case List(a) => problem(pos, s"expected arguments <text>: $a")
          case _       => problem(pos, "expected arguments <text>")
    ,
    new Command("frame", 2, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(c: AST, a: AST) =>
            val hbox = new HBox

            context.asInstanceOf[Compositor].hbox(hbox)
            renderer.render(a)
            context.asInstanceOf[Compositor].modeStack.pop()
            context.asInstanceOf[Compositor].add(new FrameBox(hbox) { border = Color(renderer.eval(c).toString) })
          case _ => problem(pos, "expected arguments <color> <text>")
    ,
    new Command("background", 4, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(c: AST, a: AST, p: AST, material: AST) =>
            val color = renderer.eval(c).toString
            val alpha = renderer.eval(a).asInstanceOf[Number].doubleValue()
            val pad = renderer.eval(a).asInstanceOf[Number].doubleValue()
            val hbox = new HBox

            context.asInstanceOf[Compositor].hbox(hbox)
            renderer.render(material)
            context.asInstanceOf[Compositor].modeStack.pop()
            context
              .asInstanceOf[Compositor]
              .add(new FrameBox(hbox) {
                background = Color(color, alpha)
                rounded = false
                padding(pad)
              })
          case _ => problem(pos, "expected arguments <color> <text>"),
  )
