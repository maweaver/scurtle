 package com.scurtle.ide

import java.awt.Insets
import javax.swing.JToolBar
import scala.swing.{Action, Button, Component}

class ScurtleToolbar(frame: ScurtleIdeFrame)
extends Component {
  override lazy val peer = new JToolBar()
  
  def createButton(action: Action) {
    val button = new Button(action)
    button.text = ""
    button.margin = new Insets(0, 0, 0, 0)
    peer.add(button.peer)
  }
  
  createButton(frame.newAction)
  createButton(frame.openAction)
  createButton(frame.saveAction)
  createButton(frame.saveAsAction)
  peer.addSeparator();
  createButton(frame.cutAction)
  createButton(frame.copyAction)
  createButton(frame.pasteAction)
  peer.addSeparator()
  createButton(frame.runAction)
  createButton(frame.debugAction)
  createButton(frame.stopDebuggingAction)
  peer.addSeparator()
  createButton(frame.nextCommandAction)
  createButton(frame.firstCommandAction)
  createButton(frame.continueAction)
}
