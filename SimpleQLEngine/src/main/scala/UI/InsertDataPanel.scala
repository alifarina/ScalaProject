package UI

import java.awt.Color

import Functional.SqlMethodsClass
import javax.swing.BorderFactory

import scala.swing._

class InsertDataPanel {
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

    val title = new Label("     ")
    val tableNameLabel = new Label("Write your query here")
    val queryBox = new TextArea(2,20)
    val executeButton = new Button("Execute >")

    listenTo(executeButton)
    //column,row
    add(title, constraints(3, 0, gridwidth = 1, fill = GridBagPanel.Fill.Horizontal))
    add(tableNameLabel, constraints(0, 2, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(queryBox, constraints(3, 2, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(executeButton, constraints(0, 3, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    val columnEntryPanel = new GridPanel(1, 2) // make it 1,2 later and add datatypes dropdown
    columnEntryPanel.border = BorderFactory.createBevelBorder(5)
    add(columnEntryPanel, constraints(0, 5, gridwidth = 1, gridheight = 4, fill = GridBagPanel.Fill.Horizontal))
    reactions += {
      case event.ButtonClicked(b) =>
        if (b.text == "Execute >") {
          val label = new Label("Count: ")

          val c2 = new Constraints()
          c2.gridx = 20
          c2.gridy = 20
          c2.weightx = 0.5
          add(label, c2)
          val query = queryBox.peer.getText()
          label.peer.setText(query)
          columnEntryPanel.rows=columnEntryPanel.rows+1
          columnEntryPanel.contents.addOne(label)
          val output=SqlMethodsClass.processInsertOrThrow(query)
          println(output)
        }
        if (b.text == "Remove Column") {
          val columnCount = columnEntryPanel.contents.length
          columnEntryPanel.rows=columnEntryPanel.rows-1
          columnEntryPanel.contents.remove(columnCount - 1)
        }
        columnEntryPanel.repaint()
        columnEntryPanel.revalidate()
    }
  }

  def ReturnPanel(): Panel = {
    panel.border=BorderFactory.createLineBorder(Color.BLACK,1)
    return panel
  }

  def removePanelFromPanel(p: Panel): Unit = {

  }
}
