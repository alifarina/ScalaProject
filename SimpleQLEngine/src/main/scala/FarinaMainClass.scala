import Errors.GeneralException
import Utils.{AppUtils, StateObjects}

import scala.util.Either

object FarinaMainClass {

  def main(args: Array[String]): Unit = {
    val tableName = "Student"
    val columnNames = List("name", "department", "email")


    // adding error handling
    def createTableOrThorow: Either[GeneralException, String] = {
      val source = Math.round(Math.random * 100)
      if (AppUtils.isValidString(tableName)){
        val tableStudent = new CreateTable("Student", columnNames)
        val mapCreated = tableStudent.create();
        println(mapCreated) // map of table name and columns
        Right("Table is successfully created!")
      }
      else Left(new GeneralException("Table name empty or null"))
    }


    val myValue = List("Farina Ali", "IMC E5", "farina17ali@gmail.com")

    def insertValueOrThrow(tableName:String,colNames:List[String],values:List[String]) : Either[GeneralException,String]={
      // let values be
     if(AppUtils.isValidString(tableName) && colNames.size>0 && values.size>0 && colNames.size==values.size){
       val query = new SimpleSQL().useTable(tableName).inColumns(colNames).withValues(values).
         useMap(StateObjects.tableToColMap).build()
       if(query) Right("success")
       else Left(new GeneralException("Failed to insert"))

     } else Left(new GeneralException("Table name empty or null"))


    }


    //val columnIndex= List(0,2);
    //println("query >>>", query(tableName).map(x=>columnIndex.map(i=>x(i))).filter(x=>x.contains("Farina Ali")))


  }

}
