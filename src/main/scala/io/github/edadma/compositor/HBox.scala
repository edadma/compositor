package io.github.edadma.compositor

class HBox extends ListBox:
  def naturalWidth: Double = sum(_.width)

  def naturalAscent: Double = max(_.ascent)

  def naturalDescent: Double = max(_.descent)

  override val isHorizontal: Boolean = true

  def length: Double = naturalWidth

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cx = x

      boxes foreach { b =>
        b.draw(comp, cx, y)
        cx += b.width
      }

  def setToWidth(width: Double): Unit = set(width)

  def setToHeight(height: Double): Unit = {}

  override def toString: String = s"HBox($height, $descent, ${boxes.mkString("[", ", ", "]")})"
