package UI

import java.awt.Color

import Functional.SqlMethodsClass
import Functional.SqlMethodsClass.{returnAllTableNames, selectAllOrThrow}
import javax.swing.BorderFactory

import scala.swing._

class ViewTablePanel {
  val panel = new GridBagPanel {
    def constraints(x: Int, y: Int,
                    gridwidth: Int = 1, gridheight: Int = 1,
                    weightx: Double = 0.0, weighty: Double = 0.0,
                    fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None)
    : Constraints = {
      val c = new Constraints
      c.gridx = x
      c.gridy = y
      c.gridwidth = gridwidth
      c.gridheight = gridheight
      c.weightx = weightx
      c.weighty = weighty
      c.fill = fill
      c
    }

    val title = new Label("View Table")
    val tableNameLabel = new Label("Enter Table Name")
    //val tableNameTextBox = new TextField()
    val tableNamesObject = returnAllTableNames()
    var tableNames=List("")
    if(tableNamesObject.isRight){
      tableNames=tableNamesObject.right.get
    }
    var tableNamesDropdown = new ComboBox[String](tableNames)
    val showTable = new Button("Show Table")
    listenTo(showTable)
    //column,row

    add(tableNameLabel, constraints(0, 1, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    if (tableNamesObject.isRight) {
      add(title, constraints(3, 0, gridwidth = 1, fill = GridBagPanel.Fill.Horizontal))
      add(tableNamesDropdown, constraints(3, 1, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
      //add(tableNameTextBox, constraints(3, 1, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
      add(showTable, constraints(0, 3, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    }
    else {
      tableNameLabel.text="No Tables Available to show"
    }

    val columnEntryPanel = new GridPanel(1, 1) // make it 1,2 later and add datatypes dropdown
    columnEntryPanel.border = BorderFactory.createBevelBorder(5)
    add(columnEntryPanel, constraints(0, 5, gridwidth = 4, gridheight = 4, fill = GridBagPanel.Fill.Vertical))

    def displayTable(tableName: String, list: List[String]): String = {
      var builder = new StringBuilder("")
      builder = builder.append("table Found >>> ")
      builder = builder.append("<br>----------------")
      builder = builder.append("<br>" + tableName)
      builder = builder.append("<br>----------------")
      for (x <- list) yield {
        builder = builder.append("<br>|--" + x)
        builder = builder.append("<br>----------------")
      }
      builder.toString()
    }

    reactions += {
      case event.ButtonClicked(b) =>
        val items=columnEntryPanel.contents.size
        for(i<-Range(0,items)){
          columnEntryPanel.contents.remove(i)
        }
        columnEntryPanel.rows=1
        if (b.text == "Show Table") {
          val label = new Label("Count: ")
          val c2 = new Constraints()
          c2.gridx = 20
          c2.gridy = 20
          c2.weightx = 0.5
          add(label, c2)
          //val tableName = tableNameTextBox.peer.getText()
          val tableName = tableNamesDropdown.peer.getSelectedItem.toString
          val columnN = List("*")

          val tableEntries = selectAllOrThrow(tableName, columnN)
          val htmlString = SqlMethodsClass.getTableHtmlString(tableEntries)
          htmlString match {
            case Right(x) => {
              label.peer.setText("<html>" + x + "</html>")
            }
            case Left(x) => label.peer.setText(x.getMessage)
          }
          columnEntryPanel.rows = columnEntryPanel.rows + 1
          columnEntryPanel.contents.addOne(label)
        }
        if (b.text == "Remove Column") {
          val columnCount = columnEntryPanel.contents.length
          columnEntryPanel.rows = columnEntryPanel.rows - 1
          columnEntryPanel.contents.remove(columnCount - 1)
        }
        columnEntryPanel.repaint()
        columnEntryPanel.revalidate()
    }
  }

  def ReturnPanel(): Panel = {
    panel.border = BorderFactory.createLineBorder(Color.BLACK, 1)
    panel
  }

  def removePanelFromPanel(p: Panel): Unit = {

  }
}
