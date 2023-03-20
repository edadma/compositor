package io.github.edadma.compositor

import io.github.edadma.char_reader.CharReader
import io.github.edadma.texish.{Active, Command, Parser, Renderer}

val commands =
  List(
    new Command("verses", 0):
      def apply(
          pos: CharReader,
          renderer: Renderer,
          args: List[Any],
          optional: Map[String, Any],
          context: AnyRef,
      ): Any =
        args match
          case List(a: String) =>
          case List(a, b)      => problem(pos, s"expected arguments <number> <number>: $a, $b")
          case _               => problem(pos, "expected arguments <number> <number>"),
  )
