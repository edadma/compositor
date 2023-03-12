package io.github.edadma.compositor

import java.io.FileOutputStream
import scala.util.Using
import java.nio.file.Files

def app(config: Config): Unit =
  val input =
    config match
      case Config(None, _, _, _, _, _) => scala.io.Source.stdin.mkString
      case Config(Some(file), _, _, _, _, _) =>
        val path = file.toPath.normalize.toAbsolutePath

        if !Files.exists(path) then problem(s"'$path' not found")
        else if !Files.isReadable(path) then problem(s"'$path' is not readable")
        else if !Files.isRegularFile(path) then problem(s"'$path' is not a file")
        else new String(Files.readAllBytes(file.toPath))
  val output =
    config match
      case Config(_, None, _, _, _, _) => Console.out
      case Config(_, Some(file), _, _, _, _) =>
        val path = file.toPath.normalize.toAbsolutePath

        if !Files.isWritable(path) then problem(s"'$path' is not writable")
        else new FileOutputStream(file)

//      case Config(input, output, "pdf", paper, _, _) => null
//      case Config(input, output, "png", _, resolution, size) =>
//        println(input.get.toPath) // Using(scala.io.Source.fromFile(".gitignore"))(_.mkString).get
