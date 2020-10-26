import java.util

import Utils.StateObjects

import scala.collection.mutable


class CreateTable(val tableName : String, val listOfColumns : List[String]) {

  // Creating empty HashMap
  //var hashMap = new mutable.HashMap[String,List[String]]()

  /** adding value which is a list of column names of that particular table
   * mapping table name -> column names
   */
  // using static global hashmap
  StateObjects.tableToColMap(tableName) = listOfColumns;

  def create(): mutable.HashMap[String,List[String]] = {
    println("1 table created >>> "+tableName)
    println("----------------")
    println(tableName)
    println("----------------")
    for (x <- listOfColumns) yield {
      println("| "+x+" |")
      println("----------------")
    }
       StateObjects.tableToColMap
  }

  def insert(): Unit ={

  }

}
