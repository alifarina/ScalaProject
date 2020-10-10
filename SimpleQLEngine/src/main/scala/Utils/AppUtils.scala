package Utils

object AppUtils {

  def isValidString(str: String): Boolean = {
    str match {
      case null => false;
      case str.trim.length == 0 => false
      case default => true
    }
  }

  def extractFromQuery(query: String) {
    val queryParts = query.split(" ") // List[String]
    queryParts[0] match {
      case "insert" || "INSERT" =>
      case "select" || "SELECT" =>
      case "delete" || "DELETE" =>
      case default => "Not supported currently"
    }

  }

}
