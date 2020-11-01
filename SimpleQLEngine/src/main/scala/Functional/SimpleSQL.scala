package Functional

import Common.Constants

import scala.collection.mutable


class SimpleSQL extends QueryBuilder {

  override var tableName: String = ""
  override var columns: List[String] = _
  override var values: List[String] = _
  override var hashMapData: mutable.HashMap[String, List[List[String]]] = _
  override var hashMapColumns: mutable.HashMap[String, List[String]] = _

  override def useMap(mapOfTableNameValues: mutable.HashMap[String, List[List[String]]],mapOfTableNameColumns: mutable.HashMap[String, List[String]]): QueryBuilder = {
    this.hashMapData = mapOfTableNameValues
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
  override def deleteAll(): Boolean = {
    //    val valueMap = new mutable.HashMap[String, List[List[String]]]()
    if(hashMapData.size>=1) {
      try{
        hashMapData(tableName) = List()
        true
      }
      catch {
        case _ =>false
      }
    }
    else{
      false
    }
  }
  override def drop(): Boolean = {
      try{
        hashMapData.remove(tableName)
        hashMapColumns.remove(tableName)
        true
      }
      catch {
        case _ =>false
      }
  }
  override def selectAll(): List[List[String]] = {
    if(columns(0)=="*"){
      if(hashMapData.size==0){
        var lst=List(List(Constants.NoData))
        lst=lst.prependedAll(List(hashMapColumns(tableName)))
        lst
      }
      else if(hashMapData(tableName)==null || hashMapData(tableName).size==0){
        var lst=List(List(Constants.NoData))
        lst=lst.prependedAll(List(hashMapColumns(tableName)))
        lst
      }
      else{
        var lst=hashMapData(tableName)
        lst=lst.prependedAll(List(hashMapColumns(tableName)))
        lst
      }
    }
    else {
      var columnIndexes=List[Int]()
      val allColumns=hashMapColumns(tableName)
      for (columnVal <- columns){
        val indx=allColumns.indexOf(columnVal)
        if(indx!=(-1)){ // value exist
          columnIndexes::=indx
        }
      }
//      columns.foreach((item)=>{
//        val indx=allColumns.indexOf(item)
//        if(indx!=(-1)){
//          columnIndexes::=indx
//        }
//      })
      if(hashMapData.size>0){
        var lst=hashMapData(tableName).map(x=>columnIndexes.reverse.map(i=> {
          x(i)
        }))
        lst=lst.prependedAll(List(columns))
        lst
      }
      else{
        var lst=List(List(Constants.NoData))
        lst=lst.prependedAll(List(columns))
        lst
      }

    }
  }

  override def selectAllWithFilter(): List[List[String]] = {
     hashMapData(tableName)
  }

}
