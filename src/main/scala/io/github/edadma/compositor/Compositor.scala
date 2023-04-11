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

import pprint.pprintln

abstract class Compositor private[compositor]:
  protected[compositor] val surface: Surface
  protected[compositor] val ctx: Context
  val pageWidth: Double
  val pageHeight: Double
  val imageScaling: Double
  val pageFactory: (Compositor, Double, Double) => PageBox

//  println(pageWidth)
//  println(pageHeight)

  case class Typeface(fonts: mutable.HashMap[Set[String], FontFace], baseline: Option[Double], ligatures: Set[String])

  protected val typefaces = new mutable.HashMap[String, Typeface]
  private val freetype = initFreeType.getOrElse(sys.error("error initializing FreeType"))

  // all ligatures: "\uFB03\uFB04\uFB01\uFB02\uFB00"

  loadTypeface(
    "noto",
    "fonts/NotoSerif/NotoSerif",
    "\uFB03\uFB04\uFB01\uFB02",
    Set(),
    "Regular",
    "Bold",
    "Italic",
    ("Bold", "Italic"),
  )
  //  loadTypeface("charis", "fonts/CharisSIL-6.200/CharisSIL", "Regular", "Bold", "Italic", ("Bold", "Italic"))
//  overrideBaseline("charis", 0.8)
//  loadFont("galatia", "fonts/GalSIL21/GalSILR.ttf")
//  loadFont("galatia", "fonts/GalSIL21/GalSILB.ttf", "bold")
  loadTypeface(
    "noto",
    "fonts/NotoSans/NotoSans",
    "\uFB03\uFB04\uFB01\uFB02\uFB00",
    Set("sans"),
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
    "\uFB03\uFB04\uFB01\uFB02\uFB00",
    Set(),
    "Regular",
    "Bold",
    "Italic",
    ("Bold", "Italic"),
  )
  loadTypeface(
    "gentiumbook",
    "fonts/GentiumBookPlus/GentiumBookPlus",
    "\uFB03\uFB04\uFB01\uFB02\uFB00",
    Set(),
    "Regular",
    "Bold",
    "Italic",
    ("Bold", "Italic"),
  )
  overrideBaseline("gentium", 0.8)
  loadTypeface("pt", "fonts/PTSansNarrow/PTSansNarrow", "\uFB01\uFB02", Set(), "Regular", "Bold")
  loadTypeface(
    "mono",
    "fonts/JetBrainsMono/static/JetBrainsMono",
    "",
    Set(),
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
    "alegreya",
    "fonts/Alegreya/static/Alegreya",
    "\uFB01\uFB02",
    Set(),
    "Black",
    ("Black", "Italic"),
    "Bold",
    ("Bold", "Italic"),
    "ExtraBold",
    ("ExtraBold", "Italic"),
    "Italic",
    "Medium",
    ("Medium", "Italic"),
    "Regular",
    "SemiBold",
    ("SemiBold", "Italic"),
  )
  loadTypeface(
    "alegreya",
    "fonts/AlegreyaSC/AlegreyaSC",
    "\uFB01\uFB02",
    Set("smallcaps"),
    "Black",
    ("Black", "Italic"),
    "Bold",
    ("Bold", "Italic"),
    "ExtraBold",
    ("ExtraBold", "Italic"),
    "Italic",
    "Medium",
    ("Medium", "Italic"),
    "Regular",
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

  protected[compositor] val modeStack = new mutable.Stack[Mode]
  protected[compositor] val pages = new ArrayBuffer[PageBox]

  modeStack push new DocumentMode(this)
  modeStack push new PageMode(this, pageFactory(this, pageWidth, pageHeight))

  var indentParagraph: Boolean = true
  var parindent: Double = 20
  var currentSupFont: Font = makeFont("pt", 14 * .583, "bold")
  var currentFont: Font = makeFont("alegreya", 14)
  var currentColor: Color = Color(0, 0, 0, 1)
  var ligatures: Boolean = true
  var representations: Boolean = false

//  def startPage(newpage: PageBox): Unit =
//    pageStack push State(page, firstParagraph)
//    page = newpage
//
//  def endPage: PageBox =
//    val res = page
//
//    if boxes.nonEmpty then paragraph()
//
//    page.set()
//    page = pageStack.top.page
//    firstParagraph = pageStack.top.firstParagraph
//    pageStack.pop
//    res

  def loadTypeface(
      typeface: String,
      basepath: String,
      ligatures: String,
      every: Set[String],
      styles: (Product | String)*,
  ): Unit =
    for style <- styles do
      val (styleName, styleSet) =
        style match
          case s: String  => (s, if s.toLowerCase == "regular" then Set.empty else Set(s.toLowerCase))
          case p: Product => (p.productIterator.mkString, p.productIterator.map(_.toString.toLowerCase).toSet)

      loadFont(
        typeface,
        s"$basepath-$styleName.ttf",
        ligatures map (_.toString) toSet,
        styleSet ++ every.map(_.toLowerCase),
      )

  def loadFont(typeface: String, path: String, ligatures: Set[String], style: String*): Unit =
    loadFont(typeface, path, ligatures, style.toSet)

  def loadFont(typeface: String, path: String, ligatures: Set[String], styleSet: Set[String]): Unit =
    val face = fontFaceCreateForFTFace(
      freetype
        .newFace(path, 0)
        .getOrElse(sys.error(s"error loading face: $path"))
        .faceptr,
      0,
    )

    typefaces get typeface match
      case None => typefaces(typeface) = Typeface(mutable.HashMap(styleSet -> face), None, ligatures)
      case Some(Typeface(fonts, _, ligatures)) =>
        if fonts contains styleSet then
          sys.error(s"font for typeface '$typeface' with style '${styleSet.mkString(", ")}' has already been loaded")
        else fonts(styleSet) = face

  def overrideBaseline(typeface: String, baseline: Double): Unit =
    typefaces get typeface match
      case None                                => sys.error(s"typeface '$typeface' not found")
      case Some(Typeface(fonts, _, ligatures)) => typefaces(typeface) = Typeface(fonts, Some(baseline), ligatures)

  def add(box: Box): Unit = modeStack.top add box

  def done(): Unit =
    paragraph()
    modeStack.top.done()

  def add(text: String): Unit = add(textBox(text))

  def textBox(text: String): CharBox =
    val rep =
      if representations then Ligatures.replace(text, Ligatures.REPRESENTATIONS, currentFont.ligatures) else text

    charBox(if ligatures then Ligatures(rep, currentFont.ligatures) else rep)

  def addText(text: String): Unit =
    val words = text.split(' ').filterNot(_ == "")

    words foreach add

//  def rule(width: Double, height: Double): Unit =
//    add(new FrameBox(new RigidBox(width, height)) { background = currentColor })

  def selectFont(f: Font): Unit =
    if currentFont ne f then
      currentFont = f
      currentFont match
        case t: ToyFont    => ctx.selectFontFace(t.family, t.slant, t.weight)
        case l: LoadedFont => ctx.setFontFace(l.fontFace)

      ctx.setFontSize(currentFont.size)

  def selectFont(family: String, size: Double, style: String*): Font = selectFont(family, size, style.toSet)

  def selectFont(family: String, size: Double, styleSet: Set[String]): Font =
    currentFont = makeFont(family, size, styleSet)
    currentFont

  private def makeFont(family: String, size: Double, style: String*): Font =
    makeFont(family, size, style.toSet)

  private def makeFont(family: String, size: Double, styleSet: Set[String]): Font =
    var slant = FontSlant.NORMAL
    var weight = FontWeight.NORMAL
    var fontFace: FontFace = null.asInstanceOf[FontFace]
    val (toy, baseline, ligatures) = typefaces get family match
      case None =>
        if styleSet("italic") then slant = FontSlant.ITALIC
        else if styleSet("oblique") then slant = FontSlant.OBLIQUE

        if styleSet("bold") then weight = FontWeight.BOLD

        ctx.selectFontFace(family, slant, weight)
        (true, None, null)
      case Some(Typeface(fonts, baseline, ligatures)) =>
        val face = fonts.getOrElse(
          styleSet map (_.toLowerCase) filterNot (_ == "regular"),
          sys.error(
            s"font for typeface '$family' with style '${styleSet.mkString(", ")}' has not been loaded",
          ),
        )

        fontFace = face
        ctx.setFontFace(fontFace)
        (false, baseline, ligatures)

    ctx.setFontSize(size)

    val TextExtents(_, _, _, _, _Width, _) = ctx.textExtents("_")
    val TextExtents(_, _, _, _, _sWithSpaceWidth, _) = ctx.textExtents("_ _")
    val extents = ctx.fontExtents

    if toy then new ToyFont(family, size, extents, _sWithSpaceWidth - 2 * _Width, styleSet, slant, weight)
    else new LoadedFont(family, size, extents, _sWithSpaceWidth - 2 * _Width, styleSet, fontFace, baseline, ligatures)

  def center(text: String): Unit =
    hbox()
    add(new HSpaceBox(1))
    add(textBox(text))
    add(new HSpaceBox(1))
    done()

  def hbox(): Unit = modeStack push new HBoxMode(this)

  def vspace(space: Double): Unit = add(new VSpaceBox(0, space, 0))

  def indent(): Unit =
    indentParagraph = true
    start()

  def noindent(): Unit =
    indentParagraph = false
    start()

  def italic(): Unit = addStyle("italic")

  def noitalic(): Unit = removeStyle("italic")

  def bold(): Unit = addStyle("bold")

  def nobold(): Unit = removeStyle("bold")

  def smallcaps(): Unit = addStyle("smallcaps")

  def nosmallcaps(): Unit = removeStyle("smallcaps")

  def setStyle(style: Set[String]): Font = selectFont(currentFont.family, currentFont.size, style)

  def setStyle(style: String*): Font = setStyle(style.toSet)

  def addStyle(style: String*): Font = setStyle(currentFont.style ++ style)

  def removeStyle(style: String*): Font = setStyle(currentFont.style -- style)

  def regular(): Font = selectFont(currentFont.family, currentFont.size)

  def typeface(name: String): Font = selectFont(name, currentFont.size, currentFont.style)

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
    add(hbox)

  def charBox(s: String): CharBox = new CharBox(this, s)

  def glyphBox(s: String): GlyphBox = new GlyphBox(this, s)

  def start(): Unit =
    paragraph()

    modeStack.top match
      case p: PageMode => p.start
      case _           =>

  def paragraph(): Unit =
    modeStack.top match
      case p: ParagraphMode => p.done()
      case _                =>

  def page(width: Double, height: Option[Double] = None): Unit =
    modeStack push new PageMode(this, new SimplePage(width, height))

  def hfil(): Unit = add(new HSpaceBox(1))

  def vfil(): Unit = add(new VSpaceBox(1))

  def output(): Unit =
    while modeStack.nonEmpty do done()

    for (p, i) <- pages.zipWithIndex do
      p.set()
      paintBackground()
      p.draw(this, 0, p.baselineAscent)
      emit(i)

  def paintBackground(): Unit

  def emit(idx: Int): Unit

  def destroy(): Unit =
    ctx.destroy()
    surface.destroy()
end Compositor

class PDFCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    val pageWidth: Double,
    val pageHeight: Double,
    val imageScaling: Double,
    val pageFactory: (Compositor, Double, Double) => PageBox,
) extends Compositor:
  color("black")

  def paintBackground(): Unit = {}

  def emit(idx: Int): Unit = ctx.showPage()

class PNGCompositor private[compositor] (
    protected[compositor] val surface: Surface,
    protected[compositor] val ctx: Context,
    path: String,
    val pageWidth: Double,
    val pageHeight: Double,
    val imageScaling: Double,
    val pageFactory: PageFactory,
) extends Compositor:
  color("white")

  def paintBackground(): Unit = {}
//    color("black")
//    ctx.rectangle(0, 0, pageWidth, pageHeight)
//    ctx.fill()

  def emit(idx: Int): Unit = surface.writeToPNG(path)

object Compositor:
  def pdf(
      path: String,
      paper: Paper,
      imageScaling: Double,
      pageFactory: PageFactory = simplePageFactory(),
  ): Compositor =
    val surface = pdfSurfaceCreate(path, paper.width, paper.height)
    val context = surface.create

    new PDFCompositor(surface, context, paper.width, paper.height, imageScaling, pageFactory)

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
      1 / pixelsPerPoint,
      pageFactory,
    )
end Compositor
