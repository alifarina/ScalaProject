import java.util

import scala.collection.mutable

abstract class QueryBuilder {

  var tableName: String
  var columns: List[String]
  var values: List[String]
  var mapOfTableNameColums: mutable.HashMap[String, List[String]]

  def useTable(tableName: String): QueryBuilder

  def inColumns(columns: List[String]): QueryBuilder

  def withValues(topping: List[String]): QueryBuilder

  def useMap(mapOfTableNameColums: mutable.HashMap[String, List[String]]): QueryBuilder

  def build() : mutable.HashMap[String,List[List[String]]]

}
