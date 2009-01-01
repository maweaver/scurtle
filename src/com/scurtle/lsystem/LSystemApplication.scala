package com.scurtle.lsystem

abstract class LSystemApplication 
extends ScurtleApplication {
  val lsystem: LSystem
  
  val numIterations: Int
  
  def execute() {
    for(i <- 0 until numIterations) lsystem.iterate()
    lsystem.execute()
  }
}
