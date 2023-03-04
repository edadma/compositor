package io.github.edadma.compositor

import io.github.edadma.libcairo.{FontExtents, FontSlant, FontWeight}

abstract class Font:
  val family: String
  val size: Double
  val extents: FontExtents
  val space: Double
  val style: Set[String]

class ToyFont private[compositor] (
    val family: String,
    val size: Double,
    val extents: FontExtents,
    val space: Double,
    val style: Set[String],
    val slant: FontSlant,
    val weight: FontWeight,
) extends Font
