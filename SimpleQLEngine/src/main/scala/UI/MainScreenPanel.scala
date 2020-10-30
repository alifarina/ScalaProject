package UI

import java.awt.Dimension

import scala.swing._

class MainScreenPanel {
  val panel = new GridBagPanel {
    def constraints(x: Int, y: Int,
                    gridwidth: Int = 1, gridheight: Int = 1,
                    weightx: Double = 0.0, weighty: Double = 0.0,
                    fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.Both)
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
    val insertDataButton = new Button("Query Executor")
    val console = new TextArea(10,100)
    listenTo(createTableButton)
    listenTo(viewTablesButton)
    listenTo(insertDataButton)
    //column,row
    add(title,constraints(4,0, fill=GridBagPanel.Fill.Horizontal))
    add(createTableButton,constraints(0,1,gridwidth=2, fill=GridBagPanel.Fill.Horizontal))
    add(viewTablesButton,constraints(3,1,gridwidth=2, fill=GridBagPanel.Fill.Horizontal))
    add(insertDataButton,constraints(6,1,gridwidth=2, fill=GridBagPanel.Fill.Horizontal))
    add(console,constraints(0,10,gridwidth = 100,gridheight = 100,fill=GridBagPanel.Fill.Horizontal))


    reactions+={
      case event.ButtonClicked(b) =>{
        if(b.text=="Create Table"){
          if(_contents.length>4)
            _contents.remove(_contents.length-1)
          add(new CreateTableScreenPanel().ReturnPanel(),constraints(0,3,gridwidth=80,gridheight = 100 ,fill=GridBagPanel.Fill.Horizontal))
          repaint()
          revalidate()
        }
        else if(b.text=="View Tables"){
          println(_contents.length)
          if(_contents.length>4)
          _contents.remove(_contents.length-1)
          add(new ViewTablePanel().ReturnPanel(),constraints(0,3,gridwidth=8,gridheight = 10 ,fill=GridBagPanel.Fill.Horizontal))
          repaint()
          revalidate()
        }
        else if(b.text=="Query Executor"){
          println(_contents.length)
          if(_contents.length>4)
            _contents.remove(_contents.length-1)
          add(new InsertDataPanel().ReturnPanel(),constraints(0,3,gridwidth=8,gridheight = 10 ,fill=GridBagPanel.Fill.Horizontal))
          repaint()
          revalidate()
        }
      }
    }
    preferredSize=new Dimension(500,500)
    //CreateTableScreenPanel
  }

  def AddPanelToFrame(f: Frame): Unit = {
    f.contents = panel
  }
}

