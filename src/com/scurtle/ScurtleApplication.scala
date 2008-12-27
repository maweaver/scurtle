package com.scurtle

import com.scurtle.script.{TurtleMachine, TurtleParser}

import scala.swing.MainFrame

class ScurtleApplication {
  def fieldSize = 150
  def screenSize = 2 * fieldSize
  
  val turtle = new Turtle()
  val field = new Field(fieldSize, turtle)
  val machine = new TurtleMachine()
  val parser = new TurtleParser()
    
  val window = new MainFrame {
    preferredSize = (screenSize, screenSize)
    contents = field
    visible = true
  }	
}
