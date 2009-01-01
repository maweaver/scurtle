package com.scurtle.lsystem

abstract class Generator(turtle: Turtle) {

  def generate(): List[Generator]
  
  def execute(): Unit
  
  def ~(that: Generator): List[Generator] = this :: that :: Nil
  def ~(that: List[Generator]): List[Generator] = this :: that
}

object Generator {
  def apply(turtle: Turtle, executor: () => Unit): Generator = {
    new Generator(turtle) {
      override def generate(): List[Generator] = List(this)
      
      override def execute() { executor() }
    }    
  }
  
  def apply(turtle: Turtle, executor: () => Unit, generator: () => List[Generator]): Generator = {
    new Generator(turtle) {
      override def generate(): List[Generator] = generator()
      
      override def execute() { executor() }
    }
  }
}