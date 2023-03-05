package io.github.edadma.compositor

import io.github.edadma.compositor
import io.github.edadma.freetype.initFreeType
import io.github.edadma.libcairo.{
  Context,
  FontFace,
  FontSlant,
  FontWeight,
  Format,
  Surface,
  TextExtents,
  fontFaceCreateForFTFace,
  imageSurfaceCreate,
  pdfSurfaceCreate,
}

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

abstract class Compositor private[compositor]:
  protected[compositor] val surface: Surface
  protected[compositor] val ctx: Context
  val pageWidth: Double
  val pageHeight: Double
  val pageFactory: (Compositor, Double, Double) => PageBox

  protected val fontfaces = new mutable.HashMap[String, mutable.HashMap[Set[String], FontFace]]
  private val freetype = initFreeType.getOrElse(sys.error("error initializing FreeType"))

  loadFont("galatia", "GalSIL21/GalSILR.ttf")
  loadFont("galatia", "GalSIL21/GalSILB.ttf", "bold")
  loadFont("pragati", "PragatiNarrow/PragatiNarrow-Regular.ttf")
  loadFont("pragati", "PragatiNarrow/PragatiNarrow-Bold.ttf", "bold")

  protected val boxes = new ArrayBuffer[Box]
  protected var firstParagraph: Boolean = true

  var indent: Boolean = true
  var parindent: Double = 36

  protected[compositor] var currentSupFont: Font = makeFont("pragati", 12 * .583, "bold")
  protected[compositor] var currentFont: Font = makeFont("galatia", 12)
  protected var currentColor: Color = new Color(0, 0, 0)
  protected var page: PageBox = pageFactory(this, pageWidth, pageHeight)

  def loadFont(typeface: String, path: String, style: String*): Unit =
    val styleSet = style.toSet
    val face = fontFaceCreateForFTFace(
      freetype
        .newFace(path, 0)
        .getOrElse(sys.error(s"error loading face: $path"))
        .faceptr,
      0,
    )

    fontfaces get typeface match
      case None => fontfaces(typeface) = mutable.HashMap(styleSet -> face)
      case Some(t) =>
        if t contains styleSet then
          sys.error(s"font for typeface '$typeface' with style '${style.mkString(", ")}' has already been loaded")
        else t(styleSet) = face

  def setPage(box: PageBox): Unit = page = box

  def add(box: Box): Unit = page add box

  val `LEFT SINGLE QUOTATION MARK` = "\u2018"
  val `RIGHT SINGLE QUOTATION MARK` = "\u2019"
  val `LEFT DOUBLE QUOTATION MARK` = "\u201C"
  val `RIGHT DOUBLE QUOTATION MARK` = "\u201D"

  def addWord(text: String): Unit =
    addBox(textBox(text))

  def textBox(text: String): CharBox =
    val text1 =
      text
        .replace("``", `LEFT DOUBLE QUOTATION MARK`)
        .replace("''", `RIGHT DOUBLE QUOTATION MARK`)
        .replace("`", `LEFT SINGLE QUOTATION MARK`)
        .replace("'", `RIGHT SINGLE QUOTATION MARK`)

    charBox(text1)

  def addText(text: String): Unit =
    val words = text.split(' ').filterNot(_ == "")

    words foreach addWord

  def addBox(box: Box): Unit =
    if boxes.nonEmpty then
      val space =
        boxes.last match
          case b: CharBox
              if b.text.nonEmpty &&
                !(b.text.last == '.' && Abbreviation(b.text.dropRight(1))) &&
                ".!?:;".contains(b.text.last) =>
            currentFont.space * 1.5
          case _ => currentFont.space

      boxes += new HSpaceBox(
        0,
        space,
      )
    else if indent && !firstParagraph then boxes += new RigidBox(width = parindent)

    boxes += box
  end addBox

  private def setFont(): Unit =
    currentFont match
      case t: ToyFont    => ctx.selectFontFace(t.family, t.slant, t.weight)
      case l: LoadedFont => ctx.setFontFace(l.fontFace)

    ctx.setFontSize(currentFont.size)

  def selectFont(f: Font): Unit =
    if currentFont ne f then
      currentFont = f
      setFont()

  def selectFont(family: String, size: Double, style: String*): Font = selectFont(family, size, style.toSet)

  def selectFont(family: String, size: Double, styleSet: Set[String]): Font =
    currentFont = makeFont(family, size, styleSet)
    setFont()
    currentFont

  def font(family: String, size: Double, style: String*): Font = font(family, size, style.toSet)

  def font(family: String, size: Double, styleSet: Set[String]): Font =
    val res = makeFont(family, size, styleSet)

    setFont()
    res

  private def makeFont(family: String, size: Double, style: String*): Font = makeFont(family, size, style.toSet)

  private def makeFont(family: String, size: Double, styleSet: Set[String]): Font =
    var slant = FontSlant.NORMAL
    var weight = FontWeight.NORMAL
    var fontFace: FontFace = null.asInstanceOf[FontFace]
    val toy = fontfaces get family match
      case None =>
        if styleSet("italic") then slant = FontSlant.ITALIC
        else if styleSet("oblique") then slant = FontSlant.OBLIQUE

        if styleSet("bold") then weight = FontWeight.BOLD

        ctx.selectFontFace(family, slant, weight)
        true
      case Some(f) =>
        fontFace = f.getOrElse(
          styleSet,
          sys.error(
            s"font for typeface '$family' with style '${styleSet.mkString(", ")}' has not been loaded",
          ),
        )
        ctx.setFontFace(fontFace)
        false

    ctx.setFontSize(size)

    val TextExtents(_, _, _, _, _Width, _) = ctx.textExtents("_")
    val TextExtents(_, _, _, _, _sWithSpaceWidth, _) = ctx.textExtents("_ _")
    val extents = ctx.fontExtents

    if toy then new ToyFont(family, size, extents, _sWithSpaceWidth - 2 * _Width, styleSet, slant, weight)
    else new LoadedFont(family, size, extents, _sWithSpaceWidth - 2 * _Width, styleSet, fontFace)

  def center(text: String): Unit = line(new HSpaceBox(1), textBox(text), new HSpaceBox(1))

  def line(text: String): Unit = page add textBox(text)

  def line(bs: Box*): Unit =
    val hbox = new HBox

    bs foreach hbox.add
    page add hbox

  def paragraph(): Unit =
    while boxes.nonEmpty do
      val hbox = new HBox

      @tailrec
      def line(): Unit =
        if boxes.nonEmpty then
          if hbox.width + boxes.head.width <= page.lineWidth then
            hbox add boxes.remove(0)
            line()
          else
            boxes.head match
              case b: CharBox =>
                b.text indexOf '-' match
                  case -1 =>
                    Hyphenation(b.text) match
                      case None =>
                      case Some(hyphenation) =>
                        var lastBefore: CharBox = null
                        var lastAfter: String = null

                        @tailrec
                        def longest(): Unit =
                          if hyphenation.hasNext then
                            val (before, after) = hyphenation.next
                            val beforeHyphen = b.newCharBox(before)

                            if hbox.width + beforeHyphen.width <= page.lineWidth then
                              lastBefore = beforeHyphen
                              lastAfter = after
                              longest()

                        longest()

                        if lastBefore ne null then
                          hbox add lastBefore
                          boxes.remove(0)
                          boxes.insert(0, b.newCharBox(lastAfter))
                    end match
                  case idx =>
                    val beforeHyphen = b.newCharBox(b.text.substring(0, idx + 1))

                    if hbox.width + beforeHyphen.width <= page.lineWidth then
                      hbox add beforeHyphen
                      boxes.remove(0)
                      boxes.insert(0, b.newCharBox(b.text.substring(idx + 1)))
              case _ =>

      line()

      if hbox.boxes.last.isSpace then hbox.boxes.remove(hbox.boxes.length - 1)
      if boxes.nonEmpty && boxes.head.isSpace then boxes.remove(0)
      if boxes.isEmpty then hbox add new HSpaceBox(2)

      page add hbox
    end while

    indent = true
    firstParagraph = false
  end paragraph

  def noindent(): Unit = indent = false

  def italic(): Font = addStyle("italic")

  def noitalic(): Font = removeStyle("italic")

  def bold(): Font = addStyle("bold")

  def nobold(): Font = removeStyle("bold")

  def addStyle(style: String): Font = selectFont(currentFont.family, currentFont.size, currentFont.style + style)

  def removeStyle(style: String): Font = selectFont(currentFont.family, currentFont.size, currentFont.style - style)

  def regular(): Font = selectFont(currentFont.family, currentFont.size)

  def size(points: Double): Font = selectFont(currentFont.family, points, currentFont.style)

  def color(c: Color): Color =
    if currentColor != c then
      currentColor = c
      ctx.setSourceRGBA(c.red, c.green, c.blue, c.alpha)

    c

  def color(r: Double, g: Double, b: Double, a: Double = 1): Color = color(new Color(r, g, b, a))

  def color(r: Int, g: Int, b: Int, a: Int): Color = color(new Color(r, g, b, a))

  def color(c: String): Color = color(Color(c))

  def prefixSup(sup: String, word: String): Unit =
    val f = currentFont
    val shift = -currentFont.size * .3333
    val hbox = new HBox

    selectFont(currentSupFont)
    hbox += new ShiftBox(charBox(sup), shift)
    selectFont(f)
    hbox += new HSpaceBox(0, 1, 0)
    hbox += charBox(word)
    addBox(hbox)

  def charBox(s: String): CharBox = new CharBox(this, s, currentFont, currentColor)

  def set(): Unit = page.set()

  def draw(): Unit =
    page.draw(this, 0, 0)
    emit()
    firstParagraph = true
    page = pageFactory(this, pageWidth, pageHeight)

  def emit(): Unit

  def destroy(): Unit =
    ctx.destroy()
    surface.destroy()
end Compositor

class PDFCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    val pageWidth: Double,
    val pageHeight: Double,
    val pageFactory: (Compositor, Double, Double) => PageBox,
) extends Compositor:
  def emit(): Unit = ctx.showPage()

class PNGCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    path: String,
    val pageWidth: Double,
    val pageHeight: Double,
    val pageFactory: PageFactory,
) extends Compositor:
  def emit(): Unit = surface.writeToPNG(path)

object Compositor:
  val pointsPerInch = 72

  def pdf(
      path: String,
      widthIn: Double,
      heightIn: Double,
      pageFactory: PageFactory = simplePageFactory(),
  ): Compositor =
    val surface = pdfSurfaceCreate(path, widthIn * pointsPerInch, heightIn * pointsPerInch)
    val context = surface.create

    new PDFCompositor(surface, context, widthIn * pointsPerInch, heightIn * pointsPerInch, pageFactory)

  def png(
      path: String,
      widthPx: Int,
      heightPx: Int,
      ppi: Double,
      pageFactory: PageFactory = simplePageFactory(),
  ): Compositor =
    val pixelsPerPoint = ppi / pointsPerInch
    val surface = imageSurfaceCreate(Format.ARGB32, widthPx, heightPx)
    val context = surface.create

    context.scale(pixelsPerPoint, pixelsPerPoint)
    new PNGCompositor(surface, context, path, widthPx / pixelsPerPoint, heightPx / pixelsPerPoint, pageFactory)
end Compositor
