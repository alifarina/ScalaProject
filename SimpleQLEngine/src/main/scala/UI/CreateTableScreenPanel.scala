package UI

import java.awt.Color

import javax.swing.BorderFactory

import scala.swing._

class CreateTableScreenPanel {
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

    val title = new Label("Create Table")
    val tableNameLabel = new Label("Enter Table Name")
    val tableNameTextBox = new TextField()

    val addColumn = new Button("Add Column")
    val removeColumn = new Button("Remove Column")
    listenTo(addColumn)
    listenTo(removeColumn)
    //column,row
    add(title, constraints(4, 0, gridwidth = 1, fill = GridBagPanel.Fill.Horizontal))
    add(tableNameLabel, constraints(0, 1, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(tableNameTextBox, constraints(3, 1, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(addColumn, constraints(0, 3, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(removeColumn, constraints(4, 3, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    val columnEntryPanel = new GridPanel(1, 1) // make it 1,2 later and add datatypes dropdown
    columnEntryPanel.border = BorderFactory.createBevelBorder(5)
    add(columnEntryPanel, constraints(0, 5, gridwidth = 1, gridheight = 4, fill = GridBagPanel.Fill.Horizontal))
    reactions += {
      case event.ButtonClicked(b) =>
        if (b.text == "Add Column") {
          val textCoumnName = new TextField(15)
          columnEntryPanel.rows=columnEntryPanel.rows+1
          columnEntryPanel.contents.addOne(textCoumnName)
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
