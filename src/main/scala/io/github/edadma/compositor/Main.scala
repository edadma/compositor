package io.github.edadma.compositor

@main def run(): Unit =
  val pdf = Compositor.pdf("test.pdf", 612, 792)
