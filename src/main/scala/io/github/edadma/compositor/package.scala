package io.github.edadma.compositor

import math.sqrt

def ppi(width: Int, height: Int, diagonal: Double): Double = sqrt(width * width + height * height) / diagonal
