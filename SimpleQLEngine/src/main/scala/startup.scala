import UI.{MainScreen, MainScreenPanel}

object startup extends App {
  var mainScreen = new MainScreen()
  mainScreen.openScreen()
  val startScreen=new MainScreenPanel()
  startScreen.AddPanelToFrame(mainScreen.returnFrame())
}
