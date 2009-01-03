package com.scurtle.samples

object Circle 
extends ScurtleApplication {
  def exec(args: Map[String, String]): Boolean = {
    
    def circle(radius: Double, numSegments: Int) {
      val circum: Double = 2.0d * Math.Pi * radius
      turtle.move(radius)
      turtle.rotate(90.0d)
      for(i <- 0 until numSegments) {
        turtle.draw(circum / numSegments.toDouble)
        turtle.rotate(360.0d / numSegments.toDouble)
      }
    }
    
    circle(50, 1000)
    true
  }
}
