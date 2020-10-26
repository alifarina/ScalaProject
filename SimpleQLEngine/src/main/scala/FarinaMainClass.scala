import Errors.GeneralException
import Utils.{AppUtils, StateObjects}

import scala.util.Either

object FarinaMainClass {

  def main(args: Array[String]): Unit = {
    val tableName = "Student"
    val columnNames = List("name", "department", "email")

    val myValue = List("Farina Ali", "IMC E5", "farina17ali@gmail.com")

    //val columnIndex= List(0,2);
    //println("query >>>", query(tableName).map(x=>columnIndex.map(i=>x(i))).filter(x=>x.contains("Farina Ali")))
    println(createTableOrThrow("Student", columnNames))
    println(processQuery("insert into Student (name,department,email) values (FarinaAli,IMcE5,farina17ali@gmail.com)"))
    
    //val columns= StateObjects.tableToColMap(tableName);
    //println(StateObjects.tableToValueMap.keys)
    //val range=0.until(columns.length)
    //StateObjects.tableToValueMap(tableName).map(x=>range.map((i)=> println(x(i))))

  }

  // adding error handling
  def createTableOrThrow(tableName: String, columnNames: List[String]): Either[GeneralException, String] = {
    println("is valid ", AppUtils.isValidString(tableName))
    if (AppUtils.isValidString(tableName)) {
      val tableStudent = new CreateTable(tableName, columnNames)
      val mapCreated = tableStudent.create();
      println(mapCreated) // map of table name and columns
      Right("Table is successfully created!")
    }
    else Left(new GeneralException("Table name empty or null"))
  }

  def insertValueOrThrow(tableName: String, colNames: List[String], values: List[String]): Either[GeneralException, String] = {
    // let values be
    if (AppUtils.isValidString(tableName) && colNames.size > 0 && values.size > 0 && colNames.size == values.size) {
      val query = new SimpleSQL().useTable(tableName).inColumns(colNames).withValues(values).
        useMap(StateObjects.tableToValueMap).build()
      if (query) Right("query successfully executed!")
      else Left(new GeneralException("Failed to insert"))

    } else Left(new GeneralException("Table name empty or null"))

  }


  def processQuery(query: String): Either[GeneralException, String] = {
    val keyword = AppUtils.extractKeywordFromQuery(query, 0)
    keyword match {
      case "insert" => processInsertOrThrow(query)
      case "select" => processInsertOrThrow(query)
      case "delete" => processInsertOrThrow(query)
      case _ => Left(new GeneralException("Not supported yet!"))
    }
  }

  def processInsertOrThrow(query: String): Either[GeneralException, String] = {
    val tableName = AppUtils.extractKeywordFromQuery(query, 2)
    val columnsArray = AppUtils.extractKeywordFromQuery(query, 3)
      .replace("(", "").replace(")", "").split(",")
    val valuesArray = AppUtils.extractKeywordFromQuery(query, 5)
      .replace("(", "").replace(")", "").split(",")
    println(tableName, "cols", columnsArray.toList, "values", valuesArray.toList)
    insertValueOrThrow(tableName, columnsArray.toList, valuesArray.toList)
  }


}
