package io.github.edadma.compositor

import scala.annotation.tailrec

object Ligatures:
  import Unicode.*

  @tailrec
  def replace(s: String, replacements: List[(String, String)]): String =
    replacements match
      case Nil                        => s
      case (target, replacement) :: t => replace(s.replace(target, replacement), t)

  def apply(s: String): String =
    if EXCEPTIONS.exists(e => s endsWith e) then s
    else replace(s, LIGATURES)

  private val LIGATURES = List(
    "ffi" -> `LATIN SMALL LIGATURE FFI`,
    "ffl" -> `LATIN SMALL LIGATURE FFL`,
//    "ff" -> `LATIN SMALL LIGATURE FF`, // noto serif didn't have this one
    "fi" -> `LATIN SMALL LIGATURE FI`,
    "fl" -> `LATIN SMALL LIGATURE FL`,
  )

  private[compositor] val REPRESENTATIONS = List(
    "``" -> `LEFT DOUBLE QUOTATION MARK`,
    "''" -> `RIGHT DOUBLE QUOTATION MARK`,
    "---" -> `EM DASH`,
    "<-->" -> `LONG LEFT RIGHT ARROW`,
    "<==>" -> `LONG LEFT RIGHT DOUBLE ARROW`,
    "<--" -> `LONG LEFTWARDS ARROW`,
    "-->" -> `LONG RIGHTWARDS ARROW`,
    "<==" -> `LONG LEFTWARDS DOUBLE ARROW`,
    "==>" -> `LONG RIGHTWARDS DOUBLE ARROW`,
    "<->" -> `LEFT RIGHT ARROW`,
    "<=>" -> `LEFT RIGHT DOUBLE ARROW`,
    "<-" -> `LEFTWARDS ARROW`,
    "->" -> `RIGHTWARDS ARROW`,
    "<=" -> `LEFTWARDS DOUBLE ARROW`,
    "=>" -> `RIGHTWARDS DOUBLE ARROW`,
    "--" -> `EN DASH`,
    "..." -> `HORIZONTAL ELLIPSIS`,
  )

  private val EXCEPTIONS = List(
    "fful",
    "fing",
    "fish",
    "fier",
    "fily",
    "finess",
    "fless",
    "fly",
    "flike",
    "flife",
    "fline",
    "flet",
    "pdflatex",
    "ffing",
    "ffish",
    "ffier",
    "ffily",
    "ffiness",
    "ffies",
    "ffian",
    "ffly",
    "ffless",
    "scofflaw",
    "cufflink",
    "offline",
    "offload",
    "fflike",
    "chaffinch",
    "wolffish",
    "safflower",
    "fteen",
    "fth",
    "ftie",
    "fty",
    "halftime",
    "halftone",
    "rooftop",
    "rooftree",
    "offtrack",
  )
