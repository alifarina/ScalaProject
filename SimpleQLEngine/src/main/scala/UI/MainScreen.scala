package UI

import scala.swing._

class MainScreen {
  var screen: Frame = new Frame {
    title = "Hello world"
//    contents = new FlowPanel {
//      contents += new Label("Launch rainbows:")
//      contents += new Button("Click me") {
//        reactions += {
//          case event.ButtonClicked(_) =>
//            println("All the colours!")
//            //new CreateTableScreen().openScreen()
//            //new MainScreenPanel().clearContentsOfFrame(screen)
//        }
//      }
//    }
    size=new Dimension(500,500)
    //pack()
    //centerOnScreen()
    open()
  }
  def returnFrame():Frame={
    return screen
  }
  def openScreen(): Unit = {
    screen.open()
  }
  def closeScreen(): Unit = {
    screen.close()
  }
}
