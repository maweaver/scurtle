package com.scurtle.lsystem

class LSystem(generators: List[Generator]) {
  
  private var currGenerators: List[Generator] = generators
  
  def iterate() {
    currGenerators = currGenerators.flatMap { g => g.generate() }
  }
  
  def execute() {
    currGenerators foreach { g => g.execute() }
  }
  
  def hasNext = !currGenerators.isEmpty
  
  def executeNext() {
    currGenerators.head.execute()
    currGenerators = currGenerators.tail
  }
  
  override def toString = currGenerators.mkString(" ")
}
