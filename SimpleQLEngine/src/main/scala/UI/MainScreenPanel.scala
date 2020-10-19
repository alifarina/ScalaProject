package UI

import scala.swing._

class MainScreenPanel {
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
    val title = new Label("Main Menu")
    val createTableButton = new Button("Create Table")
    val viewTablesButton = new Button("View Tables")
    val insertDataButton = new Button("Insert Data")
    listenTo(createTableButton)
    listenTo(viewTablesButton)
    listenTo(insertDataButton)
    //column,row
    add(title,constraints(4,0, fill=GridBagPanel.Fill.Horizontal))
    add(createTableButton,constraints(0,1,gridwidth=2, fill=GridBagPanel.Fill.Horizontal))
    add(viewTablesButton,constraints(3,1,gridwidth=2, fill=GridBagPanel.Fill.Horizontal))
    add(insertDataButton,constraints(6,1,gridwidth=2, fill=GridBagPanel.Fill.Horizontal))


    reactions+={
      case event.ButtonClicked(b) =>{
        if(b.text=="Create Table"){
          add(new CreateTableScreenPanel().ReturnPanel(),constraints(0,3,gridwidth=8,gridheight = 10 ,fill=GridBagPanel.Fill.Horizontal))
          repaint()
          revalidate()
        }
        if(b.text=="View Tables"){
          println(_contents.length)
          if(_contents.length>4)
          _contents.remove(_contents.length-1)
          repaint()
          revalidate()
        }
      }
    }
    //CreateTableScreenPanel
  }

  def AddPanelToFrame(f: Frame): Unit = {
    f.contents = panel
  }
}

