package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

import math.sqrt

def ppi(width: Int, height: Int, diagonal: Double): Double = sqrt(width * width + height * height) / diagonal

type PageFactory = (Compositor, Double, Double) => PageBox

private val numberRegex = "[0-9]+".r
private val footnoteRegex = "[^[]+\\[[a-z]]".r

//def verses(comp: Compositor, text: String): Unit =
//  val words = text.split("\\s+")
//  var i = 0
//
//  while i < words.length do
//    val word = words(i)
//
//    if numberRegex.matches(word) && i < words.length - 1 then
//      comp.prefixSup(word, words(i + 1))
//      i += 2
//    else
//      if footnoteRegex.matches(word) then comp.addWord(word.dropRight(3))
//      else comp.addWord(word)
//
//      i += 1
//
//  comp.paragraph()
//end verses

def setColor(ct: Context, c: Color): Unit = ct.setSourceRGBA(c.red, c.green, c.blue, c.alpha)

def problem(msg: String): Nothing =
  Console.err.println(msg)
  sys.exit(1)
