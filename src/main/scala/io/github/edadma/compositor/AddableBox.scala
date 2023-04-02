package io.github.edadma.compositor

trait AddableBox extends Box:
  def add(box: Box): AddableBox
  def last: Box
