package UI

import java.awt.Color
import java.util

import Functional.SqlMethodsClass
import Utils.AppUtils
import javax.swing.BorderFactory

import scala.swing.{TextField, _}

class CreateTableScreenPanel {

  var addColumnsList = new util.ArrayList[TextField]()
  var columnNames = List[String]()

  val panel = new GridBagPanel {
    def constraints(x: Int, y: Int,
                    gridwidth: Int = 1, gridheight: Int = 1,
                    weightx: Double = 0.0, weighty: Double = 0.0,
                    fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None, ipadx: Int=0, ipady: Int=0)
    : Constraints = {
      val c = new Constraints
      c.gridx = x
      c.gridy = y
      c.gridwidth = gridwidth
      c.gridheight = gridheight
      c.weightx = weightx
      c.weighty = weighty
      c.fill = fill
      c.ipadx=ipadx
      c.ipady=ipady
      c
    }

    val title = new Label("Create Table")
    val tableNameLabel = new Label("Enter Table Name")
    val tableNameTextBox = new TextField(15)
    val errorView = new Label("")

    val addColumn = new Button("Add Column")
    val removeColumn = new Button("Remove Column")
    val createButton = new Button("CREATE")
    listenTo(addColumn)
    listenTo(removeColumn)
    listenTo(createButton)
    //column,row
    add(title, constraints(4, 0, gridwidth = 1, fill = GridBagPanel.Fill.Horizontal))
    add(tableNameLabel, constraints(0, 1, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(tableNameTextBox, constraints(3, 1, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(addColumn, constraints(0, 3, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(removeColumn, constraints(4, 3, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(createButton, constraints(4, 10, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(errorView, constraints(0, 11, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    val columnEntryPanel = new GridPanel(1, 1) // make it 1,2 later and add datatypes dropdown
    columnEntryPanel.border = BorderFactory.createBevelBorder(5)
    add(columnEntryPanel, constraints(0, 5, gridwidth = 5, gridheight = 4, fill = GridBagPanel.Fill.Both,ipadx = 5))
    reactions += {
      case event.ButtonClicked(b) =>
        if (b.text == "Add Column") {
          val textCoumnName = new TextField(30)
          addColumnsList.add(textCoumnName)
          columnEntryPanel.rows=columnEntryPanel.rows+1
          columnEntryPanel.contents.addOne(textCoumnName)
        }
        else if (b.text == "Remove Column") {
          val columnCount = columnEntryPanel.contents.length
          columnEntryPanel.rows=columnEntryPanel.rows-1
          if (columnEntryPanel.contents.size>0) {
            columnEntryPanel.contents.remove(columnCount - 1)
            addColumnsList.remove(addColumnsList.size()-1)
          }

        }else if(b.text == "CREATE"){
          columnNames = List[String]()
          val tableName =tableNameTextBox.peer.getText()
         addColumnsList.forEach(tf => {
           if(AppUtils.isValidString(tf.peer.getText())){
             columnNames::=tf.peer.getText()
           }
         })
          println("All added columns >>> ",columnNames)
         val result=SqlMethodsClass.createTableOrThrow(tableName, columnNames.reverse)
           result match {
             case Right(x) => {errorView.peer.setText(x)
            //   println(processQuery("insert into "+tableName+" (name,department,email) values (FarinaAli,IMcE5,farina17ali@gmail.com)"))
            //   println(processQuery("insert into "+tableName+" (name,department,email) values (FarinaLalAli,IMcE5,farinaLal17ali@gmail.com)"))
             }
             case Left(x) =>  errorView.peer.setText(x.getMessage)
           }


        }
        columnEntryPanel.repaint()
        columnEntryPanel.revalidate()
    }
  }

  def ReturnPanel(): Panel = {
    panel.border=BorderFactory.createLineBorder(Color.BLACK,1)
    panel
  }

  def removePanelFromPanel(p: Panel): Unit = {

  }
}
