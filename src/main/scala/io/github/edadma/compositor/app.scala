package io.github.edadma.compositor

import io.github.edadma.char_reader.CharReader
import io.github.edadma.texish.{Active, Command, Parser, Renderer}

import java.io.FileOutputStream
import scala.util.Using
import java.nio.file.{Files, Paths}
import scala.collection.mutable

import pprint._

def app(config: Config): Unit =
  val input =
    config match
      case Config(None, _, _, _, _, _, _) => scala.io.Source.stdin.mkString
      case Config(Some(file), _, _, _, _, _, _) =>
        val path = file.toPath.normalize.toAbsolutePath

        if !Files.exists(path) then problem(s"'$path' not found")
        else if !Files.isReadable(path) then problem(s"'$path' is not readable")
        else if !Files.isRegularFile(path) then problem(s"'$path' is not a file")
        else new String(Files.readAllBytes(file.toPath))

  if config.multi then
    val inputs = input.split("---")
  else process(input, 0)

  def process(in: String, n: Int): Unit =
    val output =
      config match
        case Config(None, out, typ, _, _, _, _) => Paths.get(s"$out.$typ").normalize.toAbsolutePath
        case Config(Some(file), None, typ, _, _, _, _) =>
          val path = file.toPath.normalize.toAbsolutePath

          path.getParent.resolve(s"${path.getFileName.toString}.$typ")
        case Config(_, Some(file), _, _, _, _, _) => file.toPath.normalize.toAbsolutePath

    if !Files.isWritable(output.getParent) then problem(s"'$output' is not writable")

    val doc =
      config match
        case Config(_, _, "pdf", paper, _, _, _) =>
          val p =
            paper match
              case "a4"     => Paper.A4
              case "letter" => Paper.LETTER

          Compositor.pdf(output.toString, p, simplePageFactory())
        case Config(_, _, "png", _, resolution, size, _) =>
          val (width, height) =
            resolution match
              case "sd"  => (720, 480)
              case "hd"  => (1280, 720)
              case "fhd" => (1920, 1080)

          Compositor.png(output.toString, width, height, ppi(width, height, size), simplePageFactory())
        case _ => sys.error("error")
    val config =
      Map(
        "today" -> "MMMM d, y",
        "include" -> ".",
        "rounding" -> "HALF_EVEN",
      )
    val parser = new Parser(Command.builtins ++ commands, Nil, blanks = true)
    var newlineCount: Int = 0
    val out: PartialFunction[Any, Unit] = {
      case "\n" if newlineCount == 0 => newlineCount += 1
      case "\n" =>
        doc.paragraph()
        newlineCount = 0
      case s: String if s.isBlank =>
      case s: String =>
        doc.add(s)
        newlineCount = 0
    }
    val renderer = new Renderer(parser, config, _.mkString, doc, out)
    val ast = parser.parse(input)

    // pprintln(ast)
    println(s"page width = ${doc.pageWidth}")
    println(s"page height = ${doc.pageHeight}")
    renderer.render(ast)
    doc.output()
    doc.destroy()
  end process
