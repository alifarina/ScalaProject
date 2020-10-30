package Common

object HelperMethods {
  def returnTableHtml(tableEntries: List[List[String]]): String = {
    var htmlString = new StringBuilder("")
    htmlString.append("<table border='5'>")
    var index = 0
    for (row <- tableEntries) {
      htmlString.append("<tr>")
      for (col <- row) {
        if (index == 0) {
          htmlString.append("<th>" + col + "</th>")
        }
        else {
          htmlString.append("<td>" + col + "</td>")
        }
      }
      htmlString.append("</tr>")
      index = index + 1
    }
    htmlString.append("</table>")
    htmlString.toString()
  }
}

