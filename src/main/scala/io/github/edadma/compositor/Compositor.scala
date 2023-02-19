package io.github.edadma.compositor

import io.github.edadma.compositor
import io.github.edadma.libcairo.{Context, FontSlant, FontWeight, Surface, TextExtents, pdfSurfaceCreate}

import scala.collection.mutable.ArrayBuffer

class Compositor private (surface: Surface, context: Context):
  private val boxes = new ArrayBuffer[Box]
  private var currentFont: Font = new Font("sans", FontSlant.NORMAL, FontWeight.NORMAL, 10, context.fontExtents)
  private var currentColor: Color = new Color(0, 0, 0)

  def +=(text: String): Unit = add(textBox(text))

  def +=(box: Box): Unit = add(box)

  def add(box: Box): Unit =
    if boxes.nonEmpty then
      boxes.last match
        case b: TextBox =>
          if b.text.nonEmpty then
            boxes += new SpaceBox(if ".!?:" contains b.text.last then 5 else 10) // todo: use font info for spaces

          boxes += box
        case _ =>
          boxes += box
    else boxes += box

  def paragraph(width: Double): VBox =
    val hbox = new HBox

    boxes foreach (b => hbox += b)

    val vbox = new VBox

    vbox.add(hbox)
    vbox
  end paragraph

  def textBox(text: String): TextBox =
    val extents = context textExtents text

    new TextBox(text, currentFont, currentColor):
      val height: Double = currentFont.extents.height
      val descent: Double = currentFont.extents.descent
      val width: Double = extents.width // todo: may also include xBearing and/or xAdvance. not sure

  def charBox(text: String): CharBox =
    val extents = context textExtents text

    new CharBox(text, currentFont, currentColor):
      val height: Double = extents.height
      val descent: Double = 0
      val width: Double = extents.width

  def destroy(): Unit =
    context.destroy()
    surface.destroy()
end Compositor

object Compositor:
  def pdf(path: String, width: Double, height: Double): Compositor =
    val surface = pdfSurfaceCreate(path, width, height)
    val context = surface.create

    new Compositor(surface, context)
end Compositor
