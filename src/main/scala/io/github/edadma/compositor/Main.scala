package io.github.edadma.compositor

import java.io.File

case class Config(
    input: Option[File] = None,
    output: Option[File] = None,
    typ: String = "pdf",
    paper: String = "letter",
    resolution: String = "hd",
    size: Double = 13,
)

@main def run(args: String*): Unit =
  import scopt.OParser
  val builder = OParser.builder[Config]
  val parser = {
    import builder._
    OParser.sequence(
      programName("compositor"),
      head("Compositor", "0.x"),
      help("help").text("prints this usage text"),
      arg[File]("<file>")
        .optional()
        .action((x, c) => c.copy(input = Some(x)))
        .text("input file (defaults to standard input)"),
      opt[File]('o', "output")
        .valueName("<file>")
        .action((x, c) => c.copy(output = Some(x)))
        .text("output file (defaults to out.(pdf|png))"),
      opt[Int]('s', "size")
        .valueName("<inches>")
        .action((x, c) => c.copy(size = x))
        .validate(s =>
          if 0 < s then success
          else failure("only positive values are allowed as screen sizes"),
        )
        .text("screen size in inches for PNG file output (defaults to 13)"),
      opt[String]('r', "resolution")
        .valueName("<sd | hd | fhd>")
        .action((x, c) => c.copy(resolution = x))
        .validate({
          case "sd" | "hd" | "fhd" => success
          case _                   => failure("only 'sd' | 'hd' | 'fhd' are allowed as resolutions")
        })
        .text("resolution (defaults to hd)"),
      opt[String]('p', "paper")
        .valueName("<a4 | letter>")
        .action((x, c) => c.copy(paper = x))
        .validate({
          case "a4" | "letter" => success
          case _               => failure("only 'a4' or 'letter' are allowed as paper types")
        })
        .text("paper size (defaults to letter)"),
      opt[String]('t', "type")
        .valueName("<pdf | png>")
        .action((x, c) => c.copy(typ = x))
        .validate({
          case "png" | "pdf" => success
          case _             => failure("only 'png' or 'pdf' are allowed as output file types")
        })
        .text("output file type (defaults to pdf)"),
    )
  }

  OParser.parse(parser, args, Config()) match {
    case Some(config) => app(config)
    case _            =>
  }

//  val doc = Compositor.png("test.png", 1280, 720, ppi(1280, 720, 14), simplePageFactory())

//  val doc = Compositor.pdf(
//    "test.pdf",
//    Paper.LETTER,
//    simplePageFactory(),
//  )

//  doc.add("hello")

////  doc add new FrameBox(new RigidBox(doc.pageWidth, doc.pageHeight * 2 / 3)) { background = Color.TRANSPARENT }
////  doc.startPage(new SimplePage(doc.pageWidth - 10, Some(doc.pageHeight * 3 - 5)))
////  verses(
////    doc,
////    "20 But as he considered these things, behold, an angel of the Lord appeared to him in a dream, saying, “Joseph, son of David, do not fear to take Mary as your wife, for that which is conceived in her is from the Holy Spirit. 21 She will bear a son, and you shall call his name Jesus, for he will save his people from their sins.” 22 All this took place to fulfill what the Lord had spoken by the prophet:\n\n23 “Behold, the virgin shall conceive and bear a son,\n    and they shall call his name Immanuel”\n\n(which means, God with us).",
////  )
////  doc addText "Koine Greek (UK: /ˈkɔɪniː/ COY-nee[2] US: /ˈkɔɪneɪ/ COY-nay or /kɔɪˈneɪ/ coy-NAY[3][4]; Koine Greek: ἡ κοινὴ διάλεκτος, romanized: hē koinè diálektos, lit. 'the common dialect'; Greek: [i cyˈni ðiˈalektos])"
////  doc.paragraph()
////  doc.selectFont("notosans", 12)
//  doc addBox new CanvasBox(
//    doc,
//    List(
//      Paint.Extreme(0, 0),
//      Paint.Extreme(40, 40),
//      Paint.Arc(20, 20, 8, 0, 2 * math.Pi),
//      Paint.Fill,
//      Paint.Box(new GlyphBox(doc, "3", doc.currentFont, Color("black")), 20, 20, "center"),
//    ),
//  )
//  doc addText "office file waffle flux"
//  doc.paragraph()
//  doc setStyle "bold"
//  doc.noindent()
//  doc addBox new UnderlineBox(doc, (new HBox).add(doc.textBox("office")))
//  doc addBox new FrameBox(doc.textBox("file")) {
//    border = Color("green")
////    background = Color("teal")
//  }
//  doc addText "waffle flux"
//  doc.paragraph()
//  doc setStyle "italic"
//  doc.noindent()
//  doc addBox doc.glyphBox("office")
//  doc addText "file waffle flux"
//  doc.paragraph()
//  doc.setStyle("bold", "italic")
//  doc.noindent()
//  doc addText "office file waffle flux"
//  doc.paragraph()
//
//  //  doc add new VSpaceBox(1)
////  doc add new FrameBox(doc.endPage) {
////    background = Color("black", 0.5)
////    leftPadding = 5
////    rightPadding = 5
////    topPadding = 5
////  }
//  doc.output()
//  doc.destroy()
