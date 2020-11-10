package Functional

import Common.Constants
import Errors.GeneralException
import Utils.AppUtils.{ltrim, returnInfoFromInsertQuery, rtrim}
import Utils.{AppUtils, StateObjects}

import scala.collection.mutable.ListBuffer
import scala.util.Either

object SqlMethodsClass {

  def main(args: Array[String]): Unit = {
    val tableName = "student"
    val columnNames = List("name", "department", "email")

    val myValue = List("Farina Ali", "IMC E5", "farina17ali@gmail.com")
    //returnInfoFromInsertQuery("insert into Student (name,department,email) values (FarinaAli,IMcE5,farina17ali@gmail.com)")
    //val columnIndex= List(0,2);
    //println("query >>>", query(tableName).map(x=>columnIndex.map(i=>x(i))).filter(x=>x.contains("Farina Ali")))
    println(createTableOrThrow(tableName, columnNames))
    println("-------insertblock-------")
    println(processQuery("insert into Student (name,department,email) values (FarinaAli,IMcE5,farina17ali@gmail.com)"))
    println(processQuery("insert into Student (name,department,email) values (FarinaLal Ali,IMc E5,farinaLal17ali@gmail.com)"))
    println("-------selectblock---------")
    val columnN = List("email", "name")
    var filterColumnNames=List()
    var filterColumnValues=List()
    println(selectAllOrThrow(tableName, columnN,filterColumnNames,filterColumnValues))
    //val columns= StateObjects.tableToColMap(tableName);
    //println(StateObjects.tableToValueMap.keys)
    //val range=0.until(columns.length)
    //StateObjects.tableToValueMap(tableName).map(x=>range.map((i)=> println(x(i))))

  }

  def selectAllOrThrow(tableName: String, columnNames: List[String], filterColumnNames: List[String]=null,filterColumnValues:List[String]=null): Either[GeneralException, List[List[String]]] = {
    try {
      val query = new SimpleSQL().useTable(tableName).inColumns(columnNames).filterColumns(filterColumnNames).withValues(filterColumnValues).
        useMap(StateObjects.tableToValueMap, StateObjects.tableToColMap).selectAll()
      print(query)
      Right(query)
    }
    catch {
      case _ => Left(new GeneralException("Error in fetching table "))
    }
  }


  def showTableSchema(tableName: String): Either[GeneralException, List[String]] = {
    if (!AppUtils.isValidString(tableName) | !StateObjects.tableToColMap.contains(tableName)) {
      Left(new GeneralException("Table not found or name empty"))
    } else {
      //Todo : call select here in Right and return List

      Right(StateObjects.tableToColMap(tableName))
    }

  }

  def returnAllTableNames(): Either[GeneralException, List[String]] = {
    val names = StateObjects.tableToColMap.keys;
    if (names.size == 0) {
      Left(new GeneralException("No tables available"))
    }
    else {
      Right(names.toList)
    }
  }

  def getTableHtmlString(tableEntries: Either[GeneralException, List[List[String]]]): Either[GeneralException, String] = {

    if (tableEntries.isRight) {
      val htmlstring = AppUtils.returnTableHtml(tableEntries.right.get)
      if (htmlstring != "") {
        Right(htmlstring)
      }
      else {
        Left(new GeneralException("Table not found"))
      }
    }
    else {
      Left(new GeneralException("Table not found"))
    }

  }

  def dropTableOrThrow(query: String): Either[GeneralException, String] = {
    val splittedQuery = query.split(" ")
    if (splittedQuery.size != 3) {
      Left(new GeneralException(Constants.QueryFailed))
    } else if (!AppUtils.isValidString(splittedQuery(2))) {
      Left(new GeneralException(Constants.QueryFailed))
    } else {
      val querySuccess = new SimpleSQL().useTable(splittedQuery(2)).
        useMap(StateObjects.tableToValueMap, StateObjects.tableToColMap).drop()
      if (querySuccess) {
        Right(Constants.TableDropped)
      }
      else {
        Left(new GeneralException(Constants.QueryFailed))
      }
    }

  }

  def deleteTableAllRowsOrThrow(query: String): Either[GeneralException, String] = {
    val splittedQuery = query.split(" ")
    if (splittedQuery.size != 3) {
      Left(new GeneralException(Constants.QueryFailed))
    } else if (!AppUtils.isValidString(splittedQuery(2))) {
      Left(new GeneralException(Constants.QueryFailed))
    } else {
      val querySuccess = new SimpleSQL().useTable(splittedQuery(2)).
        useMap(StateObjects.tableToValueMap, StateObjects.tableToColMap).deleteAll()
      if (querySuccess) {
        Right(Constants.TableDataDeleted)
      }
      else {
        Left(new GeneralException(Constants.QueryFailed))
      }
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
    } else {
      val tableStudent = new CreateTable(tableName, columnNames)
      val mapCreated = tableStudent.create()
      println(mapCreated) // map of table name and columns
      Right("Table is successfully created!")
    }
  }

  private def insertValueOrThrow(tableName: String, colNames: List[String], values: List[String]): Either[GeneralException, String] = {
    // let values be
    if (!AppUtils.isValidString(tableName) | colNames == null
      | values == null | colNames.size == 0 | values.size == 0 | colNames.size != values.size) {
      Left(new GeneralException("Query is not correct, please check"))
    } else {
      val query = new SimpleSQL().useTable(tableName).inColumns(colNames).withValues(values).
        useMap(StateObjects.tableToValueMap, StateObjects.tableToColMap).add()
      if (query) Right("query successfully executed!")
      else Left(new GeneralException("Failed to insert"))
    }
    //    if (AppUtils.isValidString(tableName) && colNames.size > 0 && values.size > 0 && colNames.size == values.size) {
    //      val query = new SimpleSQL().useTable(tableName).inColumns(colNames).withValues(values).
    //        useMap(StateObjects.tableToValueMap, StateObjects.tableToColMap).add()
    //      if (query) Right("query successfully executed!")
    //      else Left(new GeneralException("Failed to insert"))
    //
    //    } else Left(new GeneralException("Table name empty or null"))

  }


  def processQuery(query: String): Either[GeneralException, String] = {
    val keyword = AppUtils.extractKeywordFromQuery(query, 0)
    keyword match {
      case "select" | "SELECT" => processSelectQueryAndReturnTableHtml(query)
      case "insert" | "INSERT" => processInsertOrThrow(query)
      case "delete" | "DELETE" => {
        if (query.contains("where")) {
          Left(new GeneralException(Constants.QueryNotSupported))
        } else {
          deleteTableAllRowsOrThrow(query)
        }
      }
      case "drop" | "DROP" => dropTableOrThrow(query)
      case _ => Left(new GeneralException("Not supported yet!"))
    }
  }

  def processInsertOrThrow(query: String): Either[GeneralException, String] = {
    val tp = returnInfoFromInsertQuery(query)
    val tableName = tp._1
    val columnsArray = tp._2
    val valuesArray = tp._3
    //    val tableName = AppUtils.extractKeywordFromQuery(query, 2)
    //    val columnsArray = AppUtils.extractKeywordFromQuery(query, 3)
    //      .replace("(", "").replace(")", "").split(",")
    //    val valuesArray = AppUtils.extractKeywordFromQuery(query, 5)
    //      .replace("(", "").replace(")", "").split(",")
    //    println(tableName, "cols", columnsArray.toList, "values", valuesArray.toList)
    if (StateObjects.tableToColMap != null && StateObjects.tableToColMap.contains(tableName)) {
      //checking if table to column map exists
      insertValueOrThrow(tableName, columnsArray, valuesArray)
    } else {
      Left(new GeneralException("No such table exist. Create table first."))
    }

  }

  def processSelectQueryAndReturnTableHtml(query: String): Either[GeneralException, String] = {
    val tableEntries = processSelectOrThrow(query)
    if (tableEntries.isRight) {
      val htmlString = SqlMethodsClass.getTableHtmlString(tableEntries)
      if (htmlString.isRight) {
        Right(htmlString.right.get)
      } else {
        Left(new GeneralException(Constants.QueryFailed))
      }
    }
    else {
      Left(new GeneralException(Constants.QueryFailed))
    }
  }

  def processSelectOrThrow(query: String): Either[GeneralException, List[List[String]]] = {
    val querySplit = query.split("where")
    var conditions = ""
    if (querySplit.size == 2) {
      conditions = querySplit(1)
    }
    if (querySplit(0).split(" ").size < 4) {
      Left(new GeneralException(Constants.QueryFailed))
    } else if (query.toLowerCase().contains("as") || query.toLowerCase().contains("join")) {
      Left(new GeneralException(Constants.QueryNotSupported))
    } else {
      val tableName = AppUtils.extractKeywordFromQuery(querySplit(0), 3)
      val columnsArray = AppUtils.extractKeywordFromQuery(querySplit(0), 1)
        .replace("(", "").replace(")", "").toLowerCase.split(",")
      var filterColumnNames=new ListBuffer[String]()
      var filterColumnValues=new ListBuffer[String]()
      println(tableName, "cols", columnsArray.toList, "values")
      if (conditions != "") {
        var contionsArray = conditions.split("and")
        var charS=""
        for (ent <- contionsArray) {
          if (ent.contains(">")) {
            charS=">"
          }
          else if (ent.contains("<")) {
            charS="<"
          }
          else if (ent.contains("=")) {
            charS="="
          }
          else if (ent.contains("like")) {
            charS="like"
          }
          val column = ltrim(rtrim(ent.substring(0, ent.indexOf(charS))))
          val columnValue = ltrim(rtrim(ent.substring(ent.indexOf(charS),ent.length)))
          filterColumnNames+=column
          filterColumnValues+=columnValue
        }

      }

      selectAllOrThrow(tableName, columnsArray.toList,filterColumnNames.toList,filterColumnValues.toList)
    }
  }
}
