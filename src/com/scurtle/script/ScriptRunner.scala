package com.scurtle.script

import java.io.File
import scala.io.Source
import scala.swing.MainFrame

object ScriptRunner 
extends ScurtleApplication {
  def exec(args: Map[String, String]): Boolean = {
    
    
    if(args.contains("inputFile")) {
      val inputFile = args("inputFile")
      if(new File(inputFile).exists) {
        val lines = Source.fromFile(args("inputFile")).getLines
        lines foreach { l => machine.addCommand(parser.parseLine(l).get) }
        machine.play(turtle)
        true
      } else {
        println("Could not find file " + inputFile)
        false
      }
    } else {
      println("No input file specified!")
      false
    }
  }
}
