package io.github.edadma.compositor

import math.sqrt

def ppi(width: Int, height: Int, diagonal: Double): Double = sqrt(width * width + height * height) / diagonal

type PageFactory = (Compositor, Double, Double) => PageBox

type Style = "regular" | "italic"
type Weight = "thin" | "semibold" | "medium" | "light" | "extralight" | "extrabold" | "bold" | "black"
