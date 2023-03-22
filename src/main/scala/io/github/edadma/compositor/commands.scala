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
          case _       => problem(pos, "expected arguments <text>"),
  )
