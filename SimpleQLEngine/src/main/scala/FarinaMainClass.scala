object FarinaMainClass {

  def main(args: Array[String]): Unit = {
    val tableName = "Student"
    val columnNames = List("name", "department", "email")

    val tableStudent = new CreateTable("Student", columnNames)
    val mapCreated = tableStudent.create();
    println(mapCreated) // map of table name and columns


    // let values be
    val myValue = List("Farina Ali", "IMC E5", "farina17ali@gmail.com")
    val query = new SimpleSQL().useTable(tableName).inColumns(columnNames).withValues(myValue).
      useMap(mapCreated).build();
    val columnIndex= List(0,2);
    println("query >>>", query(tableName).map(x=>columnIndex.map(i=>x(i))).filter(x=>x.contains("Farina Ali")))


  }

}
