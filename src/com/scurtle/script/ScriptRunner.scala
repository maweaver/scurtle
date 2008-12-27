package com.scurtle.script

import scala.io.Source
import scala.swing.MainFrame

object ScriptRunner 
extends ScurtleApplication {
  def main(args: Array[String]) {
    
    val lines = Source.fromFile(args(0)).getLines
    lines foreach { l => machine.addCommand(parser.parseLine(l).get) }
    machine.play(turtle)
    field.repaint()
    
  }
}
