package io.github.edadma.compositor

import io.github.edadma.char_reader.CharReader
import io.github.edadma.texish.{Active, Command, Parser, Renderer}

import java.io.FileOutputStream
import scala.util.Using
import java.nio.file.{Files, Paths}
import scala.collection.mutable

import pprint._

def app(args: Config): Unit =
  val input =
    args match
      case Config(None, _, _, _, _, _) => scala.io.Source.stdin.mkString
      case Config(Some(file), _, _, _, _, _) =>
        val path = file.toPath.normalize.toAbsolutePath

        if !Files.exists(path) then problem(s"'$path' not found")
        else if !Files.isReadable(path) then problem(s"'$path' is not readable")
        else if !Files.isRegularFile(path) then problem(s"'$path' is not a file")
        else new String(Files.readAllBytes(file.toPath))
  val output =
    args match
      case Config(None, None, typ, _, _, _) => Paths.get(s"out.$typ").normalize.toAbsolutePath
      case Config(Some(file), None, typ, _, _, _) =>
        val path = file.toPath.normalize.toAbsolutePath

        path.getParent.resolve(s"${path.getFileName.toString}.$typ")
      case Config(_, Some(file), _, _, _, _) => file.toPath.normalize.toAbsolutePath

  if !Files.isWritable(output.getParent) then problem(s"'$output' is not writable")

  val doc =
    args match
      case Config(_, _, "pdf", paper, _, _) =>
        val p =
          paper match
            case "a4"     => Paper.A4
            case "letter" => Paper.LETTER

        Compositor.pdf(output.toString, p, simplePageFactory())
      case Config(_, _, "png", _, resolution, size) =>
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
  val assigns = new mutable.HashMap[String, Any]
  val actives =
    List(
//      new Active("<") {
//        def apply(pos: CharReader, r: Renderer): String = {
//          "lt"
//        }
//      },
    )
  val parser = new Parser(Command.builtins, actives, blanks = true)
  val renderer = new Renderer(parser, config, _.mkString)
  var newlineCount: Int = 0
  val out: PartialFunction[Any, Unit] = {
    case "\n" if newlineCount == 0 => newlineCount += 1
    case "\n" =>
      doc.paragraph()
      newlineCount = 0
    case s: String if s.isBlank =>
    case s: String =>
      pprintln(s)
      doc.add(s)
      newlineCount = 0
  }

  renderer.render(parser.parse(input), assigns, out)
  doc.output()
  doc.destroy()
