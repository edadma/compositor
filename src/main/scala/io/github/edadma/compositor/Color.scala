package io.github.edadma.compositor

private val RGBRegex = "#?([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})".r

case class Color(red: Double, green: Double, blue: Double, alpha: Double):
  def this(r: Int, g: Int, b: Int, a: Int) = this(r / 255.0, g / 255.0, b / 255.0, a / 255.0)

object Color:
  def hsl(hue: Double, saturation: Double, lightness: Double, alpha: Double): Color =
    /* https://www.w3.org/TR/css-color-3/#hsl-color

      function hslToRgb (hue, sat, light) {
        hue = hue % 360;

        if (hue < 0) {
            hue += 360;
        }

        sat /= 100;
        light /= 100;

        function f(n) {
            let k = (n + hue/30) % 12;
            let a = sat * Math.min(light, 1 - light);
            return light - a * Math.max(-1, Math.min(k - 3, 9 - k, 1));
        }

        return [f(0), f(8), f(4)];
      }
     */
    val h =
      val h = hue % 360

      if h < 0 then h + 360
      else h
    val s = saturation / 100
    val l = lightness / 100

    def f(n: Double): Double =
      val k = (n + h / 30) % 12
      val a = s * math.min(l, 1 - l)

      l - a * math.max(-1, math.min(math.min(k - 3, 9 - k), 1))

    Color(f(0), f(8), f(4), alpha)
  end hsl

  def hslToRgb(hue: Double, saturation: Double, lightness: Double): (Int, Int, Int) =
    hsl(hue, saturation, lightness, 0) match
      case Color(red, green, blue, _) =>
        (
          math.min(255, (red * 255).round.toInt),
          math.min(255, (green * 255).round.toInt),
          math.min(255, (blue * 255).round.toInt),
        )

  def apply(c: String, alpha: Double = 1): Color =
    colorMap get c.toLowerCase match
      case None =>
        c match
          case RGBRegex(r, g, b) =>
            new Color(Integer.parseInt(r, 16), Integer.parseInt(g, 16), Integer.parseInt(b, 16), (alpha * 255).toInt)
          case _ => sys.error(s"color code not recognized: $c")
      case Some(color) => color.copy(alpha = alpha)

  val TRANSPARENT: Color = new Color(0, 0, 0, 0)

private val colorMap = Map(
  "aliceblue" -> new Color(0xf0, 0xf8, 0xff, 0xff),
  "antiquewhite" -> new Color(0xfa, 0xeb, 0xd7, 0xff),
  "aqua" -> new Color(0x00, 0xff, 0xff, 0xff),
  "aquamarine" -> new Color(0x7f, 0xff, 0xd4, 0xff),
  "azure" -> new Color(0xf0, 0xff, 0xff, 0xff),
  "beige" -> new Color(0xf5, 0xf5, 0xdc, 0xff),
  "bisque" -> new Color(0xff, 0xe4, 0xc4, 0xff),
  "black" -> new Color(0x00, 0x00, 0x00, 0xff),
  "blanchedalmond" -> new Color(0xff, 0xeb, 0xcd, 0xff),
  "blue" -> new Color(0x00, 0x00, 0xff, 0xff),
  "blueviolet" -> new Color(0x8a, 0x2b, 0xe2, 0xff),
  "brown" -> new Color(0xa5, 0x2a, 0x2a, 0xff),
  "burlywood" -> new Color(0xde, 0xb8, 0x87, 0xff),
  "cadetblue" -> new Color(0x5f, 0x9e, 0xa0, 0xff),
  "chartreuse" -> new Color(0x7f, 0xff, 0x00, 0xff),
  "chocolate" -> new Color(0xd2, 0x69, 0x1e, 0xff),
  "coral" -> new Color(0xff, 0x7f, 0x50, 0xff),
  "cornflowerblue" -> new Color(0x64, 0x95, 0xed, 0xff),
  "cornsilk" -> new Color(0xff, 0xf8, 0xdc, 0xff),
  "crimson" -> new Color(0xdc, 0x14, 0x3c, 0xff),
  "cyan" -> new Color(0x00, 0xff, 0xff, 0xff),
  "darkblue" -> new Color(0x00, 0x00, 0x8b, 0xff),
  "darkcyan" -> new Color(0x00, 0x8b, 0x8b, 0xff),
  "darkgoldenrod" -> new Color(0xb8, 0x86, 0x0b, 0xff),
  "darkgrey" -> new Color(0xa9, 0xa9, 0xa9, 0xff),
  "darkgreen" -> new Color(0x00, 0x64, 0x00, 0xff),
  "darkkhaki" -> new Color(0xbd, 0xb7, 0x6b, 0xff),
  "darkmagenta" -> new Color(0x8b, 0x00, 0x8b, 0xff),
  "darkolivegreen" -> new Color(0x55, 0x6b, 0x2f, 0xff),
  "darkorange" -> new Color(0xff, 0x8c, 0x00, 0xff),
  "darkorchid" -> new Color(0x99, 0x32, 0xcc, 0xff),
  "darkred" -> new Color(0x8b, 0x00, 0x00, 0xff),
  "darksalmon" -> new Color(0xe9, 0x96, 0x7a, 0xff),
  "darkseagreen" -> new Color(0x8f, 0xbc, 0x8f, 0xff),
  "darkslateblue" -> new Color(0x48, 0x3d, 0x8b, 0xff),
  "darkslategrey" -> new Color(0x2f, 0x4f, 0x4f, 0xff),
  "darkturquoise" -> new Color(0x00, 0xce, 0xd1, 0xff),
  "darkviolet" -> new Color(0x94, 0x00, 0xd3, 0xff),
  "deeppink" -> new Color(0xff, 0x14, 0x93, 0xff),
  "deepskyblue" -> new Color(0x00, 0xbf, 0xff, 0xff),
  "dimgray" -> new Color(0x69, 0x69, 0x69, 0xff),
  "dodgerblue" -> new Color(0x1e, 0x90, 0xff, 0xff),
  "firebrick" -> new Color(0xb2, 0x22, 0x22, 0xff),
  "floralwhite" -> new Color(0xff, 0xfa, 0xf0, 0xff),
  "forestgreen" -> new Color(0x22, 0x8b, 0x22, 0xff),
  "fuchsia" -> new Color(0xff, 0x00, 0xff, 0xff),
  "gainsboro" -> new Color(0xdc, 0xdc, 0xdc, 0xff),
  "ghostwhite" -> new Color(0xf8, 0xf8, 0xff, 0xff),
  "gold" -> new Color(0xff, 0xd7, 0x00, 0xff),
  "goldenrod" -> new Color(0xda, 0xa5, 0x20, 0xff),
  "grey" -> new Color(0x80, 0x80, 0x80, 0xff),
  "green" -> new Color(0x00, 0x80, 0x00, 0xff),
  "greenyellow" -> new Color(0xad, 0xff, 0x2f, 0xff),
  "honeydew" -> new Color(0xf0, 0xff, 0xf0, 0xff),
  "hotpink" -> new Color(0xff, 0x69, 0xb4, 0xff),
  "indianred" -> new Color(0xcd, 0x5c, 0x5c, 0xff),
  "indigo" -> new Color(0x4b, 0x00, 0x82, 0xff),
  "ivory" -> new Color(0xff, 0xff, 0xf0, 0xff),
  "khaki" -> new Color(0xf0, 0xe6, 0x8c, 0xff),
  "lavender" -> new Color(0xe6, 0xe6, 0xfa, 0xff),
  "lavenderblush" -> new Color(0xff, 0xf0, 0xf5, 0xff),
  "lawngreen" -> new Color(0x7c, 0xfc, 0x00, 0xff),
  "lemonchiffon" -> new Color(0xff, 0xfa, 0xcd, 0xff),
  "lightblue" -> new Color(0xad, 0xd8, 0xe6, 0xff),
  "lightcoral" -> new Color(0xf0, 0x80, 0x80, 0xff),
  "lightcyan" -> new Color(0xe0, 0xff, 0xff, 0xff),
  "lightgoldenrodyellow" -> new Color(0xfa, 0xfa, 0xd2, 0xff),
  "lightgrey" -> new Color(0xd3, 0xd3, 0xd3, 0xff),
  "lightgreen" -> new Color(0x90, 0xee, 0x90, 0xff),
  "lightpink" -> new Color(0xff, 0xb6, 0xc1, 0xff),
  "lightsalmon" -> new Color(0xff, 0xa0, 0x7a, 0xff),
  "lightseagreen" -> new Color(0x20, 0xb2, 0xaa, 0xff),
  "lightskyblue" -> new Color(0x87, 0xce, 0xfa, 0xff),
  "lightslategrey" -> new Color(0x77, 0x88, 0x99, 0xff),
  "lightsteelblue" -> new Color(0xb0, 0xc4, 0xde, 0xff),
  "lightyellow" -> new Color(0xff, 0xff, 0xe0, 0xff),
  "lime" -> new Color(0x00, 0xff, 0x00, 0xff),
  "limegreen" -> new Color(0x32, 0xcd, 0x32, 0xff),
  "linen" -> new Color(0xfa, 0xf0, 0xe6, 0xff),
  "magenta" -> new Color(0xff, 0x00, 0xff, 0xff),
  "maroon" -> new Color(0x80, 0x00, 0x00, 0xff),
  "mediumaquamarine" -> new Color(0x66, 0xcd, 0xaa, 0xff),
  "mediumblue" -> new Color(0x00, 0x00, 0xcd, 0xff),
  "mediumorchid" -> new Color(0xba, 0x55, 0xd3, 0xff),
  "mediumpurple" -> new Color(0x93, 0x70, 0xd8, 0xff),
  "mediumseagreen" -> new Color(0x3c, 0xb3, 0x71, 0xff),
  "mediumslateblue" -> new Color(0x7b, 0x68, 0xee, 0xff),
  "mediumspringgreen" -> new Color(0x00, 0xfa, 0x9a, 0xff),
  "mediumturquoise" -> new Color(0x48, 0xd1, 0xcc, 0xff),
  "mediumvioletred" -> new Color(0xc7, 0x15, 0x85, 0xff),
  "midnightblue" -> new Color(0x19, 0x19, 0x70, 0xff),
  "mintcream" -> new Color(0xf5, 0xff, 0xfa, 0xff),
  "mistyrose" -> new Color(0xff, 0xe4, 0xe1, 0xff),
  "moccasin" -> new Color(0xff, 0xe4, 0xb5, 0xff),
  "navajowhite" -> new Color(0xff, 0xde, 0xad, 0xff),
  "navy" -> new Color(0x00, 0x00, 0x80, 0xff),
  "oldlace" -> new Color(0xfd, 0xf5, 0xe6, 0xff),
  "olive" -> new Color(0x80, 0x80, 0x00, 0xff),
  "olivedrab" -> new Color(0x6b, 0x8e, 0x23, 0xff),
  "orange" -> new Color(0xff, 0xa5, 0x00, 0xff),
  "orangered" -> new Color(0xff, 0x45, 0x00, 0xff),
  "orchid" -> new Color(0xda, 0x70, 0xd6, 0xff),
  "palegoldenrod" -> new Color(0xee, 0xe8, 0xaa, 0xff),
  "palegreen" -> new Color(0x98, 0xfb, 0x98, 0xff),
  "paleturquoise" -> new Color(0xaf, 0xee, 0xee, 0xff),
  "palevioletred" -> new Color(0xd8, 0x70, 0x93, 0xff),
  "papayawhip" -> new Color(0xff, 0xef, 0xd5, 0xff),
  "peachpuff" -> new Color(0xff, 0xda, 0xb9, 0xff),
  "peru" -> new Color(0xcd, 0x85, 0x3f, 0xff),
  "pink" -> new Color(0xff, 0xc0, 0xcb, 0xff),
  "plum" -> new Color(0xdd, 0xa0, 0xdd, 0xff),
  "powderblue" -> new Color(0xb0, 0xe0, 0xe6, 0xff),
  "purple" -> new Color(0x80, 0x00, 0x80, 0xff),
  "red" -> new Color(0xff, 0x00, 0x00, 0xff),
  "rosybrown" -> new Color(0xbc, 0x8f, 0x8f, 0xff),
  "royalblue" -> new Color(0x41, 0x69, 0xe1, 0xff),
  "saddlebrown" -> new Color(0x8b, 0x45, 0x13, 0xff),
  "salmon" -> new Color(0xfa, 0x80, 0x72, 0xff),
  "sandybrown" -> new Color(0xf4, 0xa4, 0x60, 0xff),
  "seagreen" -> new Color(0x2e, 0x8b, 0x57, 0xff),
  "seashell" -> new Color(0xff, 0xf5, 0xee, 0xff),
  "sienna" -> new Color(0xa0, 0x52, 0x2d, 0xff),
  "silver" -> new Color(0xc0, 0xc0, 0xc0, 0xff),
  "skyblue" -> new Color(0x87, 0xce, 0xeb, 0xff),
  "slateblue" -> new Color(0x6a, 0x5a, 0xcd, 0xff),
  "slategrey" -> new Color(0x70, 0x80, 0x90, 0xff),
  "snow" -> new Color(0xff, 0xfa, 0xfa, 0xff),
  "springgreen" -> new Color(0x00, 0xff, 0x7f, 0xff),
  "steelblue" -> new Color(0x46, 0x82, 0xb4, 0xff),
  "tan" -> new Color(0xd2, 0xb4, 0x8c, 0xff),
  "teal" -> new Color(0x00, 0x80, 0x80, 0xff),
  "thistle" -> new Color(0xd8, 0xbf, 0xd8, 0xff),
  "tomato" -> new Color(0xff, 0x63, 0x47, 0xff),
  "turquoise" -> new Color(0x40, 0xe0, 0xd0, 0xff),
  "violet" -> new Color(0xee, 0x82, 0xee, 0xff),
  "wheat" -> new Color(0xf5, 0xde, 0xb3, 0xff),
  "white" -> new Color(0xff, 0xff, 0xff, 0xff),
  "whitesmoke" -> new Color(0xf5, 0xf5, 0xf5, 0xff),
  "yellow" -> new Color(0xff, 0xff, 0x00, 0xff),
  "yellowgreen" -> new Color(0x9a, 0xcd, 0x32, 0xff),
)
