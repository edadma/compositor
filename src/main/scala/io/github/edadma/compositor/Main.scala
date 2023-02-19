package io.github.edadma.compositor

@main def run(): Unit =
  val pdf = Compositor.pdf("test.pdf", 612, 792)

  pdf += "testing 1 - 2 - 3"
