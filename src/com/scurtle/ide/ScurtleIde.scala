package com.scurtle.ide

import javax.swing.{JToolBar}
import javax.swing.filechooser.FileNameExtensionFilter
import scala.swing.{BorderPanel, Component, Dialog, FileChooser, GUIApplication, MainFrame}
import com.scurtle.script.TurtleMachine

class ScurtleIdeFrame 
extends MainFrame {
  preferredSize = (716, 716)

  val document = new Document()
  updateTitle()
  
  document.reactions += {
    case ModifiedFlagChanged(doc) => updateTitle()
    case BackingFileChanged(doc) => updateTitle()
  }
  
  def updateTitle() {
    title = (document.backingFile match {
      case Some(file) => file.getName
      case None => "(Untitled)"
    }) + 
    (document.modified match {
      case true => "*"
      case false => ""
    }) +
    " - ScurtleIDE"
  }

  val editor = new ScurtleEditor(this) {
    preferredSize = (Int.MaxValue, 300)
  }

  val newAction = new NewAction(this)
  val openAction = new OpenAction(this)
  val saveAction = new SaveAction(this)
  val saveAsAction = new SaveAsAction(this)
  val cutAction = new CutAction(this)
  val copyAction = new CopyAction(this)
  val pasteAction = new PasteAction(this)
  val deleteAction = new DeleteAction(this)
  val selectAllAction = new SelectAllAction(this)
  val runAction = new RunAction(this)
  val debugAction = new DebugAction(this)
  val stopDebuggingAction = new StopDebuggingAction(this)
  val firstCommandAction = new FirstCommandAction(this)
  val nextCommandAction = new NextCommandAction(this)
  val continueAction = new ContinueAction(this)
  
  menuBar = new ScurtleMenu(this)
  val toolbar = new ScurtleToolbar(this)
  
  val turtle = new Turtle()
  
  val field = new Field(150, turtle)
  
  val debugInfo = new DebugInfo(this)
  
  val panel = new BorderPanel {
    add(toolbar, BorderPanel.Position.North)
    add(field, BorderPanel.Position.Center)
    add(editor, BorderPanel.Position.South)
    add(debugInfo, BorderPanel.Position.West)
  }
  
  contents = panel
  
  def newDocument() {
    warnIfModified { () =>
      document.reset()
    }
  }
  
  def openFile() {
    warnIfModified { () =>
      if(fileChooser.showOpenDialog(panel) == FileChooser.Result.Approve) {
        document.loadFrom(fileChooser.selectedFile)
      }
    }
  }
  
  def saveAs() {
    if(fileChooser.showSaveDialog(panel) == FileChooser.Result.Approve) {
      document.backingFile = Some(fileChooser.selectedFile)
    }
    document.backingFile match {
      case Some(file) => document.save()
      case None =>
    }
  }
  
  def save() {
    if(document.backingFile == None) {
      saveAs()
    }
    document.backingFile match {
      case Some(file) => document.save()
      case None =>
    }
  }
  
  def cut() = editor.cut()
  def copy() = editor.copy()
  def paste() = editor.paste()
  def delete() = editor.delete()
  def selectAll() = editor.selectAll()
  
  def setHasErrors(errors: Boolean) {
    runAction.enabled = !errors
    debugAction.enabled = !errors
  }
  
  def run() {
    turtle.reset()
    val turtleMachine = editor.createTurtleMachine()
    turtleMachine.play(turtle)
    field.repaint()
    debugInfo.turtleUpdated()
  }
  
  var debuggingTurtleMachine: TurtleMachine = _
  
  def debug() {
    stopDebuggingAction.enabled = true
    nextCommandAction.enabled = true
    firstCommandAction.enabled = true
    continueAction.enabled = true
    runAction.enabled = false
    editor.editable = false
    
    turtle.reset()
    field.repaint()

    debuggingTurtleMachine = editor.createTurtleMachine()
    document.highlightLine(debuggingTurtleMachine.pc)
    debugInfo.turtleUpdated()
  }
  
  def stopDebugging() {
    stopDebuggingAction.enabled = false
    nextCommandAction.enabled = false
    firstCommandAction.enabled = false
    continueAction.enabled = false
    runAction.enabled = true
    editor.editable = true
    document.removeHighlight()
  }
  
  def firstCommand() {
    stopDebugging()
    debug()
  }
  
  def nextCommand() {
    debuggingTurtleMachine.executeNext(turtle)
    debugInfo.turtleUpdated()
    field.repaint()
    if(debuggingTurtleMachine.done) {
      stopDebugging()
    } else {
      document.highlightLine(debuggingTurtleMachine.pc)
    }
  }
  
  def continue() {
    stopDebugging()
    run()
  }
  
  private lazy val fileChooser: FileChooser = {
    val chooser = new FileChooser()
    chooser.title = "Select a file to open"
    chooser.fileFilter = new FileNameExtensionFilter("Scurtle Files", "scurtle")
    chooser
  }
  
  def warnIfModified(fn: () => Unit) {
    if((document.modified && Dialog.showConfirmation(panel, 
                                                    "Discard unsaved changes?", 
                                                    "Are you sure?", 
                                                    Dialog.Options.YesNo, Dialog.Message.Warning
                                                    , null) == Dialog.Result.Yes) ||
         !document.modified) {
      fn()
    }

  }
}

object ScurtleIde 
extends GUIApplication {
  def main(args: Array[String]) = run {
    val frame = new ScurtleIdeFrame()
    frame.visible = true
  }
}