package com.scurtle.samples

import com.scurtle.lsystem.{Generator, LSystem, LSystemApplication}

/**
 * Based on http://en.wikipedia.org/wiki/L-System#Example_4:_Koch_curve
 */
object RightKoch 
extends LSystemApplication {
  
  val P = Generator(turtle, () => turtle.rotate(90))
  val M = Generator(turtle, () => turtle.rotate(-90))
  val F: Generator = Generator(turtle, () => turtle.draw(1), () => F::P::F::M::F::M::F::P::F::Nil)

  override val numIterations: Int = 5
  
  override val lsystem = new LSystem(List(F))
  
  def exec(args: Map[String, String]): Boolean = {
    turtle.rotate(90)
    turtle.move(115)
    turtle.rotate(180)
    execute()
    true
  }
}