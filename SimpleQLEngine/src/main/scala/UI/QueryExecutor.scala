package UI

import java.awt.Color

import Common.Constants
import Functional.SqlMethodsClass
import Utils.AppUtils
import javax.swing.BorderFactory

import scala.swing._

class QueryExecutor {
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
    add(title, constraints(3, 0, gridwidth = 5, fill = GridBagPanel.Fill.Horizontal))
    add(tableNameLabel, constraints(0, 2, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(queryBox, constraints(3, 2, gridwidth = 3, fill = GridBagPanel.Fill.Horizontal))
    add(executeButton, constraints(0, 3, gridwidth = 5, fill = GridBagPanel.Fill.Horizontal))
    val columnEntryPanel = new GridPanel(1, 2) // make it 1,2 later and add datatypes dropdown
    columnEntryPanel.border = BorderFactory.createBevelBorder(5)
    add(columnEntryPanel, constraints(0, 5, gridwidth = 5, gridheight = 4, fill = GridBagPanel.Fill.Both))
    reactions += {
      case event.ButtonClicked(b) =>
        val items=columnEntryPanel.contents.size
        for(i<-Range(0,items)){
          columnEntryPanel.contents.remove(i)
        }
        columnEntryPanel.rows=1
        if (b.text == "Execute >") {
          val label = new Label("Count: ")

          val c2 = new Constraints()
          c2.gridx = 20
          c2.gridy = 20
          c2.weightx = 0.5
          c2.gridwidth=5
          add(label, c2)
          val query = queryBox.peer.getText()
          //label.peer.setText(query)
          columnEntryPanel.rows=columnEntryPanel.rows+1
          columnEntryPanel.contents.addOne(label)
          val identifier =AppUtils.extractKeywordFromQuery(query)
          identifier match {
            case Right(x) => {
              // if match and query allowed
              if(x.equalsIgnoreCase("insert") || x.equalsIgnoreCase("delete")
                || x.equalsIgnoreCase("drop")){
                val answer=SqlMethodsClass.processQuery(query)
                answer match {
                  case Right(x) => label.peer.setText(x)
                  case Left(x) => label.peer.setText(x.getMessage)
                }
              }else{
                //select query
                val tableEntries= SqlMethodsClass.processSelectOrThrow(query)
                tableEntries match {
                  case Right(x) =>{
                    val htmlString=SqlMethodsClass.getTableHtmlString(tableEntries)
                    htmlString match {
                      case Right(x) => {label.peer.setText("<html>"+x+"</html>")}
                      case Left(x) =>  label.peer.setText(x.getMessage)
                    }
                  }
                  case Left(x) =>label.peer.setText(x.getMessage)
                }
              }
            }
            case Left(x) => label.peer.setText(x.getMessage)
          }
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
