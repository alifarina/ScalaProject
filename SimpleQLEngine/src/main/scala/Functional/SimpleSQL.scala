package Functional

import scala.collection.mutable


class SimpleSQL extends QueryBuilder {

  override var tableName: String = ""
  override var columns: List[String] = _
  override var values: List[String] = _
  override var hashMapData: mutable.HashMap[String, List[List[String]]] = _
  override var hashMapColumns: mutable.HashMap[String, List[String]] = _

  override def useMap(mapOfTableNameValues: mutable.HashMap[String, List[List[String]]],mapOfTableNameColumns: mutable.HashMap[String, List[String]]): QueryBuilder = {
    this.hashMapData = mapOfTableNameValues;
    this.hashMapColumns=mapOfTableNameColumns
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

  override def add(): Boolean = {
//    val valueMap = new mutable.HashMap[String, List[List[String]]]()
    if(hashMapData.size>=1){
      var lst=hashMapData(tableName)
      lst=lst.appendedAll(List(values))
      hashMapData(tableName)=lst
    }else{
      hashMapData(tableName) = List(values)
    }

    println(hashMapData)
    true
  }

  override def selectAll(): List[List[String]] = {
    if(columns(0)=="*"){
      return hashMapData(tableName)
    }
    else {
      var columnIndexes=List[Int]()
      val allColumns=hashMapColumns(tableName)
      columns.foreach((item)=>{
        val indx=allColumns.indexOf(item)
        if(indx!=(-1)){
          columnIndexes::=indx
        }
      })
      return hashMapData(tableName).map(x=>columnIndexes.reverse.map((i)=> {
        x(i)
      }))
    }
  }

  override def selectAllWithFilter(): List[List[String]] = {
    return hashMapData(tableName)
  }

}
