package Functional

import Errors.GeneralException
import Utils.{AppUtils, StateObjects}

import scala.util.Either

object SqlMethodsClass {

  def main(args: Array[String]): Unit = {
    val tableName = "Student"
    val columnNames = List("name", "department", "email")

    val myValue = List("Farina Ali", "IMC E5", "farina17ali@gmail.com")

    //val columnIndex= List(0,2);
    //println("query >>>", query(tableName).map(x=>columnIndex.map(i=>x(i))).filter(x=>x.contains("Farina Ali")))
    println(createTableOrThrow("Student", columnNames))
    println(processQuery("insert into Student (name,department,email) values (FarinaAli,IMcE5,farina17ali@gmail.com)"))
    println(processQuery("insert into Student (name,department,email) values (FarinaLalAli,IMcE5,farinaLal17ali@gmail.com)"))
    val columnN = List("email", "name")
    println(selectAllOrThrow("Student", columnN))
    //val columns= StateObjects.tableToColMap(tableName);
    //println(StateObjects.tableToValueMap.keys)
    //val range=0.until(columns.length)
    //StateObjects.tableToValueMap(tableName).map(x=>range.map((i)=> println(x(i))))

  }

  def selectAllOrThrow(tableName: String, columnNames: List[String]) = {
    val query = new SimpleSQL().useTable(tableName).inColumns(columnNames).
      useMap(StateObjects.tableToValueMap, StateObjects.tableToColMap).selectAll()
    print(query)
  }

  def showTableSchema(tableName: String): Either[GeneralException,List[String]] = {
    if(!AppUtils.isValidString(tableName) | !StateObjects.tableToColMap.contains(tableName)){
      Left(new GeneralException("Table not found or name empty"))
    }else {
      //Todo : call select here in Right and return List
      Right(StateObjects.tableToColMap(tableName))
    }

  }

  // adding error handling
  def createTableOrThrow(tableName: String, columnNames: List[String]): Either[GeneralException, String] = {
    println("is valid ", AppUtils.isValidString(tableName))
    if (!AppUtils.isValidString(tableName)) {
      Left(new GeneralException("Table name empty or null"))
    }
    else if (columnNames == null || columnNames.size == 0) {
      Left(new GeneralException("Adding column names mandatory"))
    }else{
      val tableStudent = new CreateTable(tableName, columnNames)
      val mapCreated = tableStudent.create()
      println(mapCreated) // map of table name and columns
      Right("Table is successfully created!")
    }
  }

  private def insertValueOrThrow(tableName: String, colNames: List[String], values: List[String]): Either[GeneralException, String] = {
    // let values be
    if (AppUtils.isValidString(tableName) && colNames.size > 0 && values.size > 0 && colNames.size == values.size) {
      val query = new SimpleSQL().useTable(tableName).inColumns(colNames).withValues(values).
        useMap(StateObjects.tableToValueMap, StateObjects.tableToColMap).add()
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
