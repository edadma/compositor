package io.github.edadma.compositor

import io.github.edadma.compositor
import io.github.edadma.libcairo.{Context, Surface, TextExtents, pdfSurfaceCreate}

import scala.collection.mutable.ArrayBuffer

class Compositor private (surface: Surface, context: Context):
  val boxes = new ArrayBuffer[Box]
  var currentFont: Font = null

  def +=(box: Box): Unit =
    if boxes.nonEmpty then
      boxes.last match
        case b: TextBox =>
          if b.text.nonEmpty then
            boxes += new SpaceBox(if ".!?:" contains text.last then 5 else 10) // todo: use font info for spaces

          boxes += box
        case _ =>
          boxes += box
    else boxes += box

  def paragraph(width: Double): VBox =
    val hbox = new HBox

    boxes foreach (b => hbox += b)

    new VBox:
      add(hbox)
  end paragraph

  def textBox(text: String): TextBox =
    val extents = context textExtents text

    new TextBox(text, currentFont):
      val height: Double = currentFont.extents.height
      val descent: Double = currentFont.extents.descent
      val width: Double = extents.width // todo: may also include xBearing and/or xAdvance. not sure

  def charBox(text: String, font: Font): CharBox =
    val extents = context textExtents text

    new CharBox(text):
      val height: Double = extents.height
      val descent: Double = 0
      val width: Double = extents.width

end Compositor

object Compositor:
  private val surfaces = new ArrayBuffer[Surface]
  private val contexts = new ArrayBuffer[Context]

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
