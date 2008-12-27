package com.scurtle.ide

import scala.swing.Action
import java.awt.event.{KeyEvent, InputEvent}
import javax.swing.{Action => SwingAction, KeyStroke, ImageIcon}

class NewAction(frame: ScurtleIdeFrame)
extends Action("New") {
    accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK))
    mnemonic = 'N'
    icon = new ImageIcon(getClass.getResource("icons/New24.gif"))
    smallIcon = new ImageIcon(getClass.getResource("icons/New16.gif"))
    toolTip = "Create a new program"

    def apply() {
      frame.newDocument()
    }
}

class OpenAction(frame: ScurtleIdeFrame)
extends Action("Open...") {
    accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK))
    mnemonic = 'O'
    icon = new ImageIcon(getClass.getResource("icons/Open24.gif"))
    smallIcon = new ImageIcon(getClass.getResource("icons/Open16.gif"))
    toolTip = "Open an existing program"

    def apply() {
      frame.openFile()
    }
}

class SaveAction(frame: ScurtleIdeFrame)
extends Action("Save") {
    accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK))
    mnemonic = 'S'
    icon = new ImageIcon(getClass.getResource("icons/Save24.gif"))
    smallIcon = new ImageIcon(getClass.getResource("icons/Save16.gif"))
    toolTip = "Save the program"
    enabled = false
    
    frame.document.reactions += {
      case ModifiedFlagChanged(doc) => enabled = doc.modified
    }

    def apply() {
      frame.save()
    }
}

class SaveAsAction(frame: ScurtleIdeFrame)
extends Action("Save As...") {
    mnemonic = 'A'
    icon = new ImageIcon(getClass.getResource("icons/SaveAs24.gif"))
    smallIcon = new ImageIcon(getClass.getResource("icons/SaveAs16.gif"))
    toolTip = "Save the program into a new file"

    def apply() {
      frame.saveAs()
    }
}

class CutAction(frame: ScurtleIdeFrame)
extends Action("Cut") {
    accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK))
    mnemonic = 'T'
    icon = new ImageIcon(getClass.getResource("icons/Cut24.gif"))
    smallIcon = new ImageIcon(getClass.getResource("icons/Cut16.gif"))
    toolTip = "Cut"

    def apply() {
      frame.cut()
    }
}

class CopyAction(frame: ScurtleIdeFrame)
extends Action("Copy") {
    accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK))
    mnemonic = 'O'
    icon = new ImageIcon(getClass.getResource("icons/Copy24.gif"))
    smallIcon = new ImageIcon(getClass.getResource("icons/Copy16.gif"))
    toolTip = "Copy"

    def apply() {
      frame.copy()
    }
}

class PasteAction(frame: ScurtleIdeFrame)
extends Action("Paste") {
    accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK))
    mnemonic = 'P'
    icon = new ImageIcon(getClass.getResource("icons/Paste24.gif"))
    smallIcon = new ImageIcon(getClass.getResource("icons/Paste16.gif"))
    toolTip = "Paste"

    def apply() {
      frame.paste()
    }
}

class DeleteAction(frame: ScurtleIdeFrame)
extends Action("Delete") {
    accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
    mnemonic = 'D'
    icon = new ImageIcon(getClass.getResource("icons/Delete24.gif"))
    smallIcon = new ImageIcon(getClass.getResource("icons/Delete16.gif"))
    toolTip = "Delete"

    def apply() {
      frame.delete()
    }
}

class SelectAllAction(frame: ScurtleIdeFrame)
extends Action("Select All") {
    accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK))
    mnemonic = 'A'
    toolTip = "Select All"

    def apply() {
      frame.selectAll()
    }
}

class RunAction(frame: ScurtleIdeFrame)
extends Action("Run") {
  accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK))
  mnemonic = 'R'
  icon = new ImageIcon(getClass.getResource("icons/Play24.gif"))
  smallIcon = new ImageIcon(getClass.getResource("icons/Play16.gif"))
  toolTip = "Run the program"
  enabled = false
 
  def apply() {
    frame.run()
  }
}

class DebugAction(frame: ScurtleIdeFrame)
extends Action("Debug") {
  accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK))
  mnemonic = 'D'
  icon = new ImageIcon(getClass.getResource("icons/Movie24.gif"))
  smallIcon = new ImageIcon(getClass.getResource("icons/Movie16.gif"))
  toolTip = "Debug the program"
  enabled = false
  
  def apply() {
    frame.debug()
  }
}

class StopDebuggingAction(frame: ScurtleIdeFrame)
extends Action("Stop Debugging") {
  mnemonic = 'S'
  icon = new ImageIcon(getClass.getResource("icons/Stop24.gif"))
  smallIcon = new ImageIcon(getClass.getResource("icons/Stop16.gif"))
  toolTip = "Debug the program"
  enabled = false
  
  def apply() {
    frame.stopDebugging()
  }
}

class FirstCommandAction(frame: ScurtleIdeFrame)
extends Action("First Command") {
  mnemonic = 'F'
  icon = new ImageIcon(getClass.getResource("icons/Rewind24.gif"))
  smallIcon = new ImageIcon(getClass.getResource("icons/Rewind16.gif"))
  toolTip = "Return to the first command"
  enabled = false
  
  def apply() {
    frame.firstCommand()
  }
}

class NextCommandAction(frame: ScurtleIdeFrame)
extends Action("Previous Command") {
  accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK))
  mnemonic = 'N'
  icon = new ImageIcon(getClass.getResource("icons/StepForward24.gif"))
  smallIcon = new ImageIcon(getClass.getResource("icons/StepForward16.gif"))
  toolTip = "Execute the next command"
  enabled = false
  
  def apply() {
    frame.nextCommand()
  }
}

class ContinueAction(frame: ScurtleIdeFrame)
extends Action("Continue") {
  mnemonic = 'C'
  icon = new ImageIcon(getClass.getResource("icons/FastForward24.gif"))
  smallIcon = new ImageIcon(getClass.getResource("icons/FastForward16.gif"))
  toolTip = "Run the program to the end"
  enabled = false
  
  def apply() {
    frame.continue()
  }
}