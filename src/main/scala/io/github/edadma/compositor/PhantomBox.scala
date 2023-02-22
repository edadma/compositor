package io.github.edadma.compositor

import io.github.edadma.libcairo.Context

class PhantomBox(box: Box) extends SameBox(box) with EmptyBox
