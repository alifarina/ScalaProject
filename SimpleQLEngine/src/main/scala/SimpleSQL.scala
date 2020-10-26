import scala.collection.mutable


class SimpleSQL extends QueryBuilder {

  override var tableName: String = ""
  override var columns: List[String] = _
  override var values: List[String] = _
  override var hasmapSimple: mutable.HashMap[String, List[List[String]]] = _

  override def useMap(mapOfTableNameColums: mutable.HashMap[String, List[List[String]]]): QueryBuilder = {
    this.hasmapSimple = mapOfTableNameColums;
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

  override def build(): Boolean = {
//    val valueMap = new mutable.HashMap[String, List[List[String]]]()
    if(hasmapSimple.size>0){
      hasmapSimple(tableName)+: List(values)
    }else{
      hasmapSimple(tableName) = List(values)
    }

    println(hasmapSimple)
    true
  }


}
