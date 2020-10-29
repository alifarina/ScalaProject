package Functional

import scala.collection.mutable

abstract class QueryBuilder {

  var tableName: String
  var columns: List[String]
  var values: List[String]
  var hashMapData: mutable.HashMap[String, List[List[String]]]
  var hashMapColumns:mutable.HashMap[String, List[String]]
  def useTable(tableName: String): QueryBuilder

  def inColumns(columns: List[String]): QueryBuilder

  def withValues(topping: List[String]): QueryBuilder

  def useMap(mapOfTableNameColums: mutable.HashMap[String, List[List[String]]],mapOfTableNameColumns: mutable.HashMap[String, List[String]]): QueryBuilder

  def add() : Boolean

  def selectAll():List[List[String]]

  def selectAllWithFilter():List[List[String]]

}
