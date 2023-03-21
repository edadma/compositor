package io.github.edadma.compositor

import io.github.edadma.char_reader.CharReader
import io.github.edadma.texish.{Active, Command, Parser, Renderer, problem}

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
          case _               => problem(pos, "expected arguments <string>"),
  )
