package io.github.edadma.compositor

import io.github.edadma.libcairo.{FontExtents, FontSlant, FontWeight}

class Font private[compositor] (
    val family: String,
    val slant: FontSlant,
    val weight: FontWeight,
    val size: Double,
    val extents: FontExtents,
    val space: Double,
)
