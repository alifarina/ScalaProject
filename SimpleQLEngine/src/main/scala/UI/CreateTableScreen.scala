package UI

import Common.Constants

import scala.swing._

class CreateTableScreen {
  var screen: Frame = new Frame {
    title=Constants.CreateTableScreenTitle
    contents = new FlowPanel {
      contents += new Label("Launch rainbows:")
    }
  }
  def openScreen(): Unit = {
    screen.open()
  }
  def closeScreen(): Unit = {
    screen.close()
  }
}
