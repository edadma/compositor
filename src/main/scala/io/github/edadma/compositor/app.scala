package io.github.edadma.compositor

import scala.util.Using
import java.nio.file.Files

def app(config: Config): Unit =
  val input =
    config match
      case Config(None, _, _, _, _, _) => scala.io.Source.stdin.mkString
      case Config(Some(file), _, _, _, _, _) =>
        val path = file.toPath.normalize.toAbsolutePath

        if !Files.exists(path) then problem(s"file '$path' not found")
        else if !Files.isReadable(path) then problem(s"file '$path' is unreadable")
        else if !Files.isRegularFile(path) then problem(s"file '$path' is not a file")
        else new String(Files.readAllBytes(file.toPath))

  println(input)
//      case Config(input, output, "pdf", paper, _, _) => null
//      case Config(input, output, "png", _, resolution, size) =>
//        println(input.get.toPath) // Using(scala.io.Source.fromFile(".gitignore"))(_.mkString).get
