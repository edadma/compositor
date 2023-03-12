package io.github.edadma.compositor

import java.io.FileOutputStream
import scala.util.Using
import java.nio.file.{Files, Paths}

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
      case Config(None, None, typ, _, _, _) => Paths.get(s"out.$typ").normalize.toAbsolutePath
      case Config(Some(file), None, typ, _, _, _) =>
        val path = file.toPath.normalize.toAbsolutePath

        path.getParent.resolve(s"${path.getFileName.toString}.$typ")
      case Config(_, Some(file), _, _, _, _) => file.toPath.normalize.toAbsolutePath

  if !Files.isWritable(output) then problem(s"'$output' is not writable")

  val doc =
    config match
      case Config(_, _, "pdf", paper, _, _) => null
      case Config(_, _, "png", _, resolution, size) =>
        Compositor.png(output.toString)
