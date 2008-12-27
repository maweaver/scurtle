package com.scurtle.ide

import scala.swing.{Menu, MenuItem, MenuBar, Orientation, Separator}

class ScurtleMenu(frame: ScurtleIdeFrame)
extends MenuBar {
  contents += new Menu("File") {
    contents += new MenuItem(frame.newAction)
    contents += new MenuItem(frame.openAction)
    contents += new MenuItem(frame.saveAction)
    contents += new MenuItem(frame.saveAsAction)
  }
  contents += new Menu("Edit") {
    contents += new MenuItem(frame.cutAction)
    contents += new MenuItem(frame.copyAction)
    contents += new MenuItem(frame.pasteAction)
    contents += new MenuItem(frame.deleteAction)
    contents += new Separator(Orientation.Horizontal)
    contents += new MenuItem(frame.selectAllAction)
  }
  contents += new Menu("Execute") {
    contents += new MenuItem(frame.runAction)
    contents += new MenuItem(frame.debugAction)
    contents += new MenuItem(frame.stopDebuggingAction)
    contents += new Separator(Orientation.Horizontal)
    contents += new MenuItem(frame.firstCommandAction)
    contents += new MenuItem(frame.nextCommandAction)
    contents += new MenuItem(frame.continueAction)
  }
}
