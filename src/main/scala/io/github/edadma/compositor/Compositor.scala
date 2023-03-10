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
  import Unicode.*

  protected[compositor] val surface: Surface
  protected[compositor] val ctx: Context
  val pageWidth: Double
  val pageHeight: Double
  val pageFactory: (Compositor, Double, Double) => PageBox

  case class Typeface(fonts: mutable.HashMap[Set[String], FontFace], baseline: Option[Double])

  protected val typefaces = new mutable.HashMap[String, Typeface]
  private val freetype = initFreeType.getOrElse(sys.error("error initializing FreeType"))

  loadTypeface("notoserif", "fonts/NotoSerif/NotoSerif", "Regular", "Bold", "Italic", ("Bold", "Italic"))
  //  loadTypeface("charis", "fonts/CharisSIL-6.200/CharisSIL", "Regular", "Bold", "Italic", ("Bold", "Italic"))
//  overrideBaseline("charis", 0.8)
//  loadFont("galatia", "fonts/GalSIL21/GalSILR.ttf")
//  loadFont("galatia", "fonts/GalSIL21/GalSILB.ttf", "bold")
  loadTypeface(
    "notosans",
    "fonts/NotoSans/NotoSans",
    "Black",
    ("Black", "Italic"),
    "Bold",
    ("Bold", "Italic"),
    "ExtraBold",
    ("ExtraBold", "Italic"),
    "ExtraLight",
    ("ExtraLight", "Italic"),
    "Italic",
    "Light",
    ("Light", "Italic"),
    "Medium",
    ("Medium", "Italic"),
    "Regular",
    "SemiBold",
    ("SemiBold", "Italic"),
    "Thin",
    ("Thin", "Italic"),
  )
  loadTypeface(
    "gentium",
    "fonts/GentiumPlus-6.200/GentiumPlus",
    "Regular",
    "Bold",
    "Italic",
    ("Bold", "Italic"),
  )
  overrideBaseline("gentium", 0.8)
  loadTypeface("pt", "fonts/PTSansNarrow/PTSansNarrow", "Regular", "Bold")
  loadTypeface(
    "mono",
    "fonts/JetBrainsMono/static/JetBrainsMono",
    "Bold",
    ("Bold", "Italic"),
    "ExtraBold",
    ("ExtraBold", "Italic"),
    "ExtraLight",
    ("ExtraLight", "Italic"),
    "Italic",
    "Light",
    ("Light", "Italic"),
    "Medium",
    ("Medium", "Italic"),
    "Regular",
    "SemiBold",
    ("SemiBold", "Italic"),
    "Thin",
    ("Thin", "Italic"),
  )
  //  loadFont("playfair", "PlayfairDisplaySC/PlayfairDisplaySC-Regular.ttf", "smallcaps")
//  loadFont("playfair", "PlayfairDisplaySC/PlayfairDisplaySC-Italic.ttf", "italic", "smallcaps")
//  loadFont("playfair", "PlayfairDisplaySC/PlayfairDisplaySC-Bold.ttf", "bold", "smallcaps")
//  loadFont("playfair", "PlayfairDisplaySC/PlayfairDisplaySC-Black.ttf", "black", "smallcaps")
//  loadFont("playfair", "PlayfairDisplaySC/PlayfairDisplaySC-BoldItalic.ttf", "bold", "italic", "smallcaps")
//  loadFont("playfair", "PlayfairDisplaySC/PlayfairDisplaySC-BlackItalic.ttf", "black", "italic", "smallcaps")
//  loadFont("playfair", "PlayfairDisplay/static/PlayfairDisplay-Regular.ttf")
//  loadFont("playfair", "PlayfairDisplay/static/PlayfairDisplay-Italic.ttf", "italic")
//  loadFont("playfair", "PlayfairDisplay/static/PlayfairDisplay-Bold.ttf", "bold")
//  loadFont("playfair", "PlayfairDisplay/static/PlayfairDisplay-Black.ttf", "black")
//  loadFont("playfair", "PlayfairDisplay/static/PlayfairDisplay-BoldItalic.ttf", "bold", "italic")
//  loadFont("playfair", "PlayfairDisplay/static/PlayfairDisplay-BlackItalic.ttf", "black", "italic")

  protected val boxes = new ArrayBuffer[Box]
  protected var firstParagraph: Boolean = true
  protected val pageStack = new mutable.Stack[State]
  protected var page: PageBox = pageFactory(this, pageWidth, pageHeight)

  case class State(page: PageBox, firstParagraph: Boolean)

  var indent: Boolean = true
  var parindent: Double = 20
  var currentSupFont: Font = makeFont("pt", 12 * .583, "bold")
  var currentFont: Font = makeFont("notoserif", 12)
  var currentColor: Color = Color(0, 0, 0, 1)
  var ligatures: Boolean = true
  var representations: Boolean = false

  def startPage(newpage: PageBox): Unit =
    pageStack push State(page, firstParagraph)
    page = newpage

  def endPage: PageBox =
    val res = page

    if boxes.nonEmpty then paragraph()

    page.set()
    page = pageStack.top.page
    firstParagraph = pageStack.top.firstParagraph
    pageStack.pop
    res

  def loadTypeface(typeface: String, basepath: String, styles: (Product | String)*): Unit =
    for style <- styles do
      val (styleName, styleSet) =
        style match
          case s: String  => (s, if s.toLowerCase == "regular" then Set.empty else Set(s.toLowerCase))
          case p: Product => (p.productIterator.mkString, p.productIterator.map(_.toString.toLowerCase).toSet)

      loadFont(
        typeface,
        s"$basepath-$styleName.ttf",
        styleSet,
      )

  def loadFont(typeface: String, path: String, style: String*): Unit = loadFont(typeface, path, style.toSet)

  def loadFont(typeface: String, path: String, styleSet: Set[String]): Unit =
    val face = fontFaceCreateForFTFace(
      freetype
        .newFace(path, 0)
        .getOrElse(sys.error(s"error loading face: $path"))
        .faceptr,
      0,
    )

    typefaces get typeface match
      case None => typefaces(typeface) = Typeface(mutable.HashMap(styleSet -> face), None)
      case Some(Typeface(fonts, _)) =>
        if fonts contains styleSet then
          sys.error(s"font for typeface '$typeface' with style '${styleSet.mkString(", ")}' has already been loaded")
        else fonts(styleSet) = face

  def overrideBaseline(typeface: String, baseline: Double): Unit =
    typefaces get typeface match
      case None                     => sys.error(s"typeface '$typeface' not found")
      case Some(Typeface(fonts, _)) => typefaces(typeface) = Typeface(fonts, Some(baseline))

  def add(box: Box): Unit = page add box

  def addWord(text: String): Unit = addBox(textBox(text))

  def textBox(text: String): CharBox =
    val rep = if representations then Ligatures.replace(text, Ligatures.REPRESENTATIONS) else text

    charBox(if ligatures then Ligatures(rep) else rep)

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

  def rule(width: Double, height: Double): Unit =
    add(new FrameBox(new RigidBox(width, height)) { background = currentColor })

  def selectFont(f: Font): Unit =
    if currentFont ne f then
      currentFont = f
      setFont()

  def selectFont(family: String, size: Double, style: String*): Font = selectFont(family, size, style.toSet)

  def selectFont(family: String, size: Double, styleSet: Set[String]): Font =
    currentFont = makeFont(family, size, styleSet)
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
    val (toy, baseline) = typefaces get family match
      case None =>
        if styleSet("italic") then slant = FontSlant.ITALIC
        else if styleSet("oblique") then slant = FontSlant.OBLIQUE

        if styleSet("bold") then weight = FontWeight.BOLD

        ctx.selectFontFace(family, slant, weight)
        (true, None)
      case Some(Typeface(fonts, baseline)) =>
        val face = fonts.getOrElse(
          styleSet,
          sys.error(
            s"font for typeface '$family' with style '${styleSet.mkString(", ")}' has not been loaded",
          ),
        )

        fontFace = face
        ctx.setFontFace(fontFace)
        (false, baseline)

    ctx.setFontSize(size)

    val TextExtents(_, _, _, _, _Width, _) = ctx.textExtents("_")
    val TextExtents(_, _, _, _, _sWithSpaceWidth, _) = ctx.textExtents("_ _")
    val extents = ctx.fontExtents

    if toy then new ToyFont(family, size, extents, _sWithSpaceWidth - 2 * _Width, styleSet, slant, weight)
    else new LoadedFont(family, size, extents, _sWithSpaceWidth - 2 * _Width, styleSet, fontFace, baseline)

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

  def setStyle(style: Set[String]): Font = selectFont(currentFont.family, currentFont.size, style)

  def setStyle(style: String*): Font = setStyle(style.toSet)

  def addStyle(style: String*): Font = setStyle(currentFont.style ++ style)

  def removeStyle(style: String*): Font = setStyle(currentFont.style -- style)

  def regular(): Font = selectFont(currentFont.family, currentFont.size)

  def size(points: Double): Font = selectFont(currentFont.family, points, currentFont.style)

  def color(c: Color): Color =
    if currentColor != c then
      currentColor = c
      setColor(c)

    c

  def setColor(c: Color): Unit = io.github.edadma.compositor.setColor(ctx, c)

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

  def charBox(s: String): CharBox = new CharBox(this, s)

  def glyphBox(s: String): GlyphBox = new GlyphBox(this, s)

  def draw(): Unit =
    if boxes.nonEmpty then paragraph()

    page.set()
    page.draw(this, 0, page.ascent)
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
  val scale: Option[Double] = None

  color("black")

  def emit(): Unit = ctx.showPage()

class PNGCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    path: String,
    val pageWidth: Double,
    val pageHeight: Double,
    val pageFactory: PageFactory,
) extends Compositor:
  color("white")

  def emit(): Unit = surface.writeToPNG(path)

object Compositor:
  def pdf(
      path: String,
      paper: Paper,
      pageFactory: PageFactory = simplePageFactory(),
  ): Compositor =
    val surface = pdfSurfaceCreate(path, paper.width, paper.height)
    val context = surface.create

    new PDFCompositor(surface, context, paper.width, paper.height, pageFactory)

  def png(
      path: String,
      widthPx: Int,
      heightPx: Int,
      ppi: Double,
      pageFactory: PageFactory = simplePageFactory(),
  ): Compositor =
    val pixelsPerPoint = ppi / Units.POINTS_PER_INCH
    val surface = imageSurfaceCreate(Format.ARGB32, widthPx, heightPx)
    val context = surface.create

    context.scale(pixelsPerPoint, pixelsPerPoint)
    new PNGCompositor(
      surface,
      context,
      path,
      widthPx / pixelsPerPoint,
      heightPx / pixelsPerPoint,
      pageFactory,
    )
end Compositor
