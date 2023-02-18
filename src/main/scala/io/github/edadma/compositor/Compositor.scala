package io.github.edadma.compositor

import io.github.edadma.compositor
import io.github.edadma.libcairo.{Context, Surface, TextExtents, pdfSurfaceCreate}

import scala.collection.mutable.ArrayBuffer

class Compositor private (surface: Surface, context: Context):
  val boxes = new ArrayBuffer[Box]
  var font: Font = null

  def +=(box: Box): Unit =
    if boxes.nonEmpty then
      boxes.last match
        case TextBox(text, font) =>
          if text.nonEmpty then
            boxes += new SpaceBox(if ".!?:" contains text.last then 5 else 10) // todo: use font info for spaces

          boxes += box
        case _ =>
          boxes += box
    else boxes += box

  def paragraph(width: Double): VBox =
    val hbox = new HBox

    hbox ++= boxes

end Compositor

object Compositor:
  val surfaces = new ArrayBuffer[Surface]
  val contexts = new ArrayBuffer[Context]

  def pdf(path: String, width: Double, height: Double): Compositor =
    val surface = pdfSurfaceCreate(path, width, height)
    val context = surface.create

    surfaces += surface
    contexts += context
    new Compositor(surface, context)

  def destroy(): Unit =
    contexts foreach (_.destroy())
    surfaces foreach (_.destroy())
end Compositor
