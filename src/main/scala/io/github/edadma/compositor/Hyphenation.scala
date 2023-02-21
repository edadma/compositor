package io.github.edadma.compositor

import scala.collection.immutable.HashMap

object Hyphenation:
  val words: Map[String, Seq[String]] =
    List(
      "al-go-rithm",
      "pres-by-te-ri-an",
      "Em-bry-ol-o-gy",
      "car-diato-my",
      "bud-get",
      "wilder-ness",
    ) map (w => w.filterNot(_ == '-') -> w.split('-').toSeq) toMap

  def apply(word: String): Option[Iterator[(String, String)]] =
    words get word map { s =>
      new Iterator[(String, String)]:
        var idx = 1

        override def hasNext: Boolean = idx < s.length

        override def next: (String, String) =
          val (before, after) = s.splitAt(idx)

          idx += 1
          (before.mkString :+ '-', after.mkString)
    }
