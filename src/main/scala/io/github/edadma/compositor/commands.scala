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
    new Command("pagebox", 3, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(w: AST, h: AST, page: AST) =>
            val width = renderer.eval(w).asInstanceOf[Number].doubleValue()
            val height = renderer.eval(h).asInstanceOf[Number].doubleValue()

            context.asInstanceOf[Compositor].page(width, Some(height))
            renderer.render(page)
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
    new Command("vfil", 0, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        context.asInstanceOf[Compositor].add(new VSpaceBox(1))
    ,
    new Command("par", 0, false):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        context.asInstanceOf[Compositor].paragraph()
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
            context.asInstanceOf[Compositor].hbox()
            renderer.render(a)
            context
              .asInstanceOf[Compositor]
              .add(
                new UnderlineBox(
                  context.asInstanceOf[Compositor],
                  context.asInstanceOf[Compositor].modeStack.pop.result,
                ),
              )
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
            context.asInstanceOf[Compositor].hbox()
            renderer.render(a)
            context
              .asInstanceOf[Compositor]
              .add(new FrameBox(context.asInstanceOf[Compositor].modeStack.pop.result) {
                border = Color(renderer.eval(c).toString)
              })
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
            val pad = renderer.eval(p).asInstanceOf[Number].doubleValue()

            context.asInstanceOf[Compositor].modeStack push new BoxMode(context.asInstanceOf[Compositor])
            renderer.render(material)
            context
              .asInstanceOf[Compositor]
              .add(new FrameBox(context.asInstanceOf[Compositor].modeStack.pop.result) {
                background = Color(color, alpha)
                rounded = false
                padding(pad)
              })
          case _ => problem(pos, "expected arguments <color> <text>")
    ,
    new Command("start", 0):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        context.asInstanceOf[Compositor].start()
    ,
    new Command("heading", 1, false):
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
            context.asInstanceOf[Compositor].bold()
            renderer.render(a)
            context.asInstanceOf[Compositor].nobold()
            context.asInstanceOf[Compositor].done()
            context.asInstanceOf[Compositor].add(new VSpaceBox(0, min = 5, stretchable = 0))
          case _ => problem(pos, "expected arguments <text>")
    ,
    new Command("font", 3, true):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(family: String, size: Number, style: String) =>
            context.asInstanceOf[Compositor].selectFont(family, size.doubleValue, style.split("\\s+").toSet)
            ()
          case _ => problem(pos, "expected arguments <family> <size> <style>")
    ,
    new Command("typeface", 1, true):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        args match
          case List(family: String) => context.asInstanceOf[Compositor].typeface(family)
          case _                    => problem(pos, "expected arguments <family>")
    ,
    new Command("indent", 0):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        context.asInstanceOf[Compositor].indent()
    ,
    new Command("noindent", 0):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: Any,
      ): Any =
        context.asInstanceOf[Compositor].noindent(),
  )
