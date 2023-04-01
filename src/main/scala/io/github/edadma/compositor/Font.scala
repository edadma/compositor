package io.github.edadma.compositor

import io.github.edadma.libcairo.{FontExtents, FontFace, FontSlant, FontWeight}

abstract class Font:
  val family: String
  val size: Double
  val extents: FontExtents
  val space: Double
  val style: Set[String]
  val baseline: Option[Double]
  val ligatures: Set[String]

  def height: Double = extents.height * baseline.getOrElse(1.0)

class ToyFont private[compositor] (
    val family: String,
    val size: Double,
    val extents: FontExtents,
    val space: Double,
    val style: Set[String],
    val slant: FontSlant,
    val weight: FontWeight,
) extends Font { val baseline: Option[Double] = None; val ligatures: Set[String] = ALL_LIGATURES }

class LoadedFont private[compositor] (
    val family: String,
    val size: Double,
    val extents: FontExtents,
    val space: Double,
    val style: Set[String],
    val fontFace: FontFace,
    val baseline: Option[Double],
    val ligatures: Set[String],
) extends Font
