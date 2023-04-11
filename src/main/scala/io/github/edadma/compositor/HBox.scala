package io.github.edadma.compositor

class HBox extends ListBox:
  def naturalWidth: Double = sum(_.width)

  def ascent: Double = naturalAscent

  def naturalAscent: Double = max(_.ascent)

  def descent: Double = naturalDescent

  def naturalDescent: Double = max(_.descent)

  def baselineAscent: Double = max(_.baselineAscent)

  def baselineHeight: Option[Double] =
    if boxes.forall(_.baselineHeight == None) then None else Some(max(_.baselineHeight getOrElse 0))

  val typ: Type = Type.Horizontal

  def pointLength: Double = naturalWidth

  def draw(comp: Compositor, x: Double, y: Double): Unit =
    if boxes.nonEmpty then
      var cx = x

      boxes foreach { b =>
        b.draw(comp, cx, y)
        cx += b.width
      }

  def set(): Unit = set(width)

  override def toString: String = s"HBox($height, $descent, ${boxes.mkString("[", ", ", "]")})"
