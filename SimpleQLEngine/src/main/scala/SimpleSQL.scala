import scala.collection.mutable


class SimpleSQL extends QueryBuilder {

  override var tableName: String = ""
  override var columns: List[String] = _
  override var values: List[String] = _
  override var mapOfTableNameColums: mutable.HashMap[String, List[String]] = _

  override def useMap(mapOfTableNameColums: mutable.HashMap[String, List[String]]): QueryBuilder = {
    this.mapOfTableNameColums = mapOfTableNameColums;
    this
  }

  override def useTable(tableName: String): QueryBuilder = {
    this.tableName = tableName
    this
  }

  override def inColumns(columns: List[String]): QueryBuilder = {
    this.columns = columns
    this
  }

  override def withValues(values: List[String]): QueryBuilder = {
    this.values = values
    this
  }

  override def build(): mutable.HashMap[String, List[List[String]]] = {
    val valueMap = new mutable.HashMap[String, List[List[String]]]()
    valueMap(tableName) = List(values)
    println(valueMap)
    valueMap
  }


}
