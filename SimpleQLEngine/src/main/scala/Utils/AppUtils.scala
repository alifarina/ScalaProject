package Utils

import Errors.GeneralException

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
      case "select" | "SELECT" => Right("insert")
      case "delete" | "DELETE" => Right("insert")
      case _ => Left(new GeneralException("Not supported currently"))
    }
  }

  def patrn(z: Option[String]) = z match
  {

    // for 'Some' class the key for
    // the given value is displayed
    case Some(s) => (s)

    // for 'None' class the below string
    // is displayed
    case None => ("key not found")
  }

}
