package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

import math.sqrt

def ppi(width: Int, height: Int, diagonal: Double): Double = sqrt(width * width + height * height) / diagonal

def pixelsToPoints(pixels: Double, ppi: Double): Double = pixels * (1 / ppi) * Units.POINTS_PER_INCH

type PageFactory = (Compositor, Double, Double) => PageBox

private val numberRegex = "[0-9]+".r
private val footnoteRegex = "[^[]+\\[[a-z]]".r

def verses(comp: Compositor, text: String): Unit =
  comp.start()

  val words = text.split("\\s+")
  var i = 0

  while i < words.length do
    val word = words(i)

    if numberRegex.matches(word) && i < words.length - 1 then
      comp.prefixSup(word, words(i + 1))
      i += 2
    else
      if footnoteRegex matches word then comp add word.dropRight(3)
      else if word.nonEmpty then
        if word == "LORD" then
          comp.smallcaps()
          comp add "Lord"
          comp.nosmallcaps()
        else comp add word

      i += 1

  comp.paragraph()
end verses

def problem(msg: String): Nothing =
  Console.err.println(msg)
  sys.exit(1)

val `LATIN SMALL LIGATURE FF` = "\uFB00"
val `LATIN SMALL LIGATURE FI` = "\uFB01"
val `LATIN SMALL LIGATURE FL` = "\uFB02"
val `LATIN SMALL LIGATURE FFI` = "\uFB03"
val `LATIN SMALL LIGATURE FFL` = "\uFB04"

val `LEFT SINGLE QUOTATION MARK` = "\u2018"
val `RIGHT SINGLE QUOTATION MARK` = "\u2019"
val `LEFT DOUBLE QUOTATION MARK` = "\u201C"
val `RIGHT DOUBLE QUOTATION MARK` = "\u201D"

val HYPHEN = "\u2010"
val `EN DASH` = "\u2013"
val `EM DASH` = "\u2014"

val `LEFTWARDS ARROW` = "\u2190"
val `RIGHTWARDS ARROW` = "\u2192"
val `LEFT RIGHT ARROW` = "\u2194"
val `LEFTWARDS DOUBLE ARROW` = "\u21D0"
val `RIGHTWARDS DOUBLE ARROW` = "\u21D2"
val `LEFT RIGHT DOUBLE ARROW` = "\u21D4"

val `LONG LEFTWARDS ARROW` = "\u27F5"
val `LONG RIGHTWARDS ARROW` = "\u27F6"
val `LONG LEFT RIGHT ARROW` = "\u27F7"
val `LONG LEFTWARDS DOUBLE ARROW` = "\u27F8"
val `LONG RIGHTWARDS DOUBLE ARROW` = "\u27F9"
val `LONG LEFT RIGHT DOUBLE ARROW` = "\u27Fa"

val `HORIZONTAL ELLIPSIS` = "\u2026"

val ALL_LIGATURES: Set[String] = Set(
  `LATIN SMALL LIGATURE FFI`,
  `LATIN SMALL LIGATURE FFL`,
  `LATIN SMALL LIGATURE FF`, // noto serif didn't have this one
  `LATIN SMALL LIGATURE FI`,
  `LATIN SMALL LIGATURE FL`,
)
