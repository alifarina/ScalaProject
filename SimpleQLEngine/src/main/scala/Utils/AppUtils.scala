package Utils

import Common.Constants
import Errors.GeneralException

import scala.util.control.Breaks.{break, breakable}

object AppUtils {

  def isValidString(str: String): Boolean = {
    str match {
      case null => return false
      case _ if str.length == 0 => return false
      case _ => return true
    }
    true
  }

  def extractKeywordFromQuery(query: String, index: Int): String = {
    val queryParts = query.split(" ") // List[String]
    val keyword = queryParts(index)
    keyword
  }

  def extractKeywordFromQuery(query: String): Either[GeneralException, String] = {
    val queryParts = query.split(" ") // List[String]
    val queryIdentifier = queryParts(0)
    queryIdentifier match {
      case "insert" | "INSERT" => Right("insert")
      case "select" | "SELECT" => Right("select")
      case "delete" | "DELETE" => Right("delete")
      case "drop" | "DROP" => Right("drop")
      case _ => Left(new GeneralException(Constants.QueryNotSupported))
    }
  }

  def patrn(z: Option[String]) = z match {

    // for 'Some' class the key for
    // the given value is displayed
    case Some(s) => (s)

    // for 'None' class the below string
    // is displayed
    case None => ("key not found")
  }

  def returnTableHtml(tableEntries: List[List[String]]): String = {
    var htmlString = new StringBuilder("")
    htmlString.append("<table border='5'>")
    var index = 0
    var headerColumnsLength=0
    for (row <- tableEntries) {
      if(headerColumnsLength==0)headerColumnsLength=row.size
      htmlString.append("<tr>")
      for (col <- row) {
        if (index == 0) {
          htmlString.append("<th>" + col + "</th>")
        }
        else {
          if(col==Constants.NoData){
            htmlString.append("<td colspan='"+headerColumnsLength+"'>" + col + "</td>")
          }
          else{
            htmlString.append("<td>" + col + "</td>")
          }

        }
      }
      htmlString.append("</tr>")
      index = index + 1
    }
    htmlString.append("</table>")
    htmlString.toString()
  }

  def returnInfoFromInsertQuery(query: String):Tuple3[String,List[String],List[String]] = {
    val totalLengthOfQuery=query.size
    var charArray = query.substring(11,totalLengthOfQuery).toCharArray()
    var values=List[String]()
    var columns=List[String]()
    var tableNameBuilder=new StringBuilder()
    var tableNameFilled = false
    var start = false
    var tableName=""
    var value = new StringBuilder()
    var quotestart = false
    var columnsPushed = false

    for (c <- charArray) {
    {
      breakable
      {
        if (!tableNameFilled) {
          if (c!='(') tableNameBuilder.append(c)
        }
        if (c=='(') {
          if(tableNameFilled==false)
          {
            tableName = rtrim(ltrim(tableNameBuilder.toString().trim()))
            tableNameFilled = true
          }
          start = true
          break
        }
        if (c==')') {
          if (!columnsPushed) columns::=value.toString().trim().toLowerCase()
          else values::=value.toString().trim()
          value = new StringBuilder()
          columnsPushed = true
          start = false
          break

        }
        if ((c=='"') && (quotestart==true)) {
          quotestart = false
          break

        }
        if (c=='"') {
          quotestart = true
          break

        }
        if ((c==',') && (quotestart==false)) {
          if (!columnsPushed) columns::=value.toString().trim()
          else values::=value.toString().trim()
          value = new StringBuilder()
          break

        }
        if (start==true) value.append(c)
      }
    }
    }
    val tp=new Tuple3[String,List[String],List[String]](tableName,columns.reverse,values.reverse)
    tp
//    Console.println("columns="+columns.reverse.mkString(","))
//    Console.println("values="+values.reverse.mkString(","))
//    Console.println("table name="+tableName)
  }
  def ltrim(s: String) = s.replaceAll("^\\s+", "")
  def rtrim(s: String) = s.replaceAll("\\s+$", "")

  val IntRegEx = "(\\d+)".r
  def getStringIntValueOrSize(s:String): Option[Int]  = s match {
    case "inf" => Some(Integer.MAX_VALUE)
    case IntRegEx(num) => Some(num.toInt)
    case _ => Some(s.size)
  }
}
