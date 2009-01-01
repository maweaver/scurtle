package com.scurtle.script

import java.awt.Color
import scala.collection.mutable.Map
import com.scurtle.lsystem.{Generator, LSystem}

class TurtleMachine {
  var commands: List[Command] = Nil
  var remembers: List[(Position, Double)] = Nil
  var repeats: List[(Int, Int)] = Nil
  val labels = Map[String, Int]()
  var returnStack: List[Int] = Nil
  var variables: List[Variable] = Nil
  var stack: List[Int] = Nil
  var lsRules = Map[String, LSGenerator]()
  var lsystem: Option[LSystem] = None
  var pc: Int = 0

  val colors = List(
    new Color(0, 0, 0),
    new Color(255, 255, 255),
    new Color(255, 0, 0),
    new Color(255, 136, 0),
    new Color(170, 170, 0),
    new Color(0, 255, 0),
    new Color(0, 255, 255),
    new Color(0, 0, 255),
    new Color(255, 0, 255)
  )
  val colorNames = Map[String, Int]()
  colorNames += "black" -> 0 
  colorNames += "white" -> 1 
  colorNames += "red" -> 2
  colorNames += "orange" -> 3 
  colorNames += "yellow" -> 4
  colorNames += "green" -> 5
  colorNames += "cyan" -> 6
  colorNames += "blue" -> 7
  colorNames += "purple" -> 8 

  var currentColor: Int = _
  
  def reset() {
    pc = 0
    repeats = Nil
    remembers = Nil
  }
  
  def addCommand(command: Command): Unit = {
    commands ::= command
    command match {
      case LabelCommand(label) =>
        labels += label -> (commands.length - 1)
      case _ =>
    }
  }
  
  def done: Boolean = pc >= commands.length
  
  def executeNext(turtle: Turtle) { 
      commands.reverse(pc) match {
        case MoveCommand(distance) => turtle.move(distance.eval(variables))
        case LeftCommand(rotation) => turtle.rotate(-rotation.eval(variables))
        case RightCommand(rotation) => turtle.rotate(rotation.eval(variables))
        case DrawCommand(distance) => turtle.draw(distance.eval(variables))
        case PointCommand(degree) => turtle.point(degree.eval(variables))
        case HomeCommand() => turtle.home
        case RememberCommand() => remembers ::= turtle.currentState
        case GoBackCommand() =>
          turtle.currentState = remembers.head
          remembers = remembers.tail
        case RepeatCommand(expr) => 
          repeats ::= (pc, expr.eval(variables) - 1)
        case NextCommand() =>
          val repeat = repeats.head
          if(repeat._2 > 0) {
            repeats = (repeat._1, repeat._2 - 1) :: repeats.tail
            pc = repeat._1
          } else {
            repeats = repeats.tail
          }
        case LabelCommand(name) =>

        case GoCommand(label) => 
          returnStack ::= pc
          pc = labels(label) 
        case ReturnCommand() =>
          pc = returnStack.head
          returnStack = returnStack.tail
        case EndCommand() => pc = commands.length          
        case NamedColorCommand(color) => 
          currentColor = colorNames(color.toLowerCase)
          turtle.color = colors(currentColor)
        case NumberedColorCommand(color) =>
          currentColor = color
          turtle.color = colors(currentColor)
        case RelativeColorCommand(offset) =>
          currentColor = (currentColor + offset) % colors.length
          turtle.color = colors(currentColor)
        case AbsoluteThicknessCommand(thickness) => turtle.thickness = thickness.eval(variables)
        case RelativeThicknessCommand(thickness) => turtle.thickness += thickness
        case AbsoluteTransparencyCommand(transparency) => turtle.alpha = 1.0f - (transparency.eval(variables).toFloat /  100f)
        case RelativeTransparencyCommand(transparency) => turtle.alpha -= transparency.toFloat / 100f
        case LetCommand(name, expr) =>
          val newVal = expr.eval(variables)
          variables = variables filter ( v => v.name != name )
          variables ::= Variable(name, newVal)
        case IfCommand(expr) => 
          if(!expr.eval(variables)) {
            while(pc < commands.length && 
                    !commands.reverse(pc).isInstanceOf[ElseCommand] && 
                    !commands.reverse(pc).isInstanceOf[EndIfCommand]) 
              pc += 1
          }
        case ElseCommand() =>
          while(pc < commands.length && 
                  !commands.reverse(pc).isInstanceOf[EndIfCommand]) 
            pc += 1
          pc -= 1
        case EndIfCommand() => 
        case PushCommand(expr) => stack ::= expr.eval(variables)
        case PopCommand(name) =>
          variables = variables filter ( v => v.name != name )
          variables ::= Variable(name, stack.head)
          stack = stack.tail
        case LSRule(name, generations) => 
          lsRules += name -> new LSGenerator(turtle, name, 
                                             if(generations.isEmpty) List(name) else generations)
        case LSRun(iterations, startRules) =>
          lsystem = lsystem match {
            case None => 
              val ls = new LSystem(startRules map { sr => lsRule(turtle, sr) })
              for(i <- 0 until iterations ) ls.iterate()
              Some(ls)
            case Some(s) => Some(s)
          }
          if(lsystem.get.hasNext) {
            lsystem.get.executeNext()
          }
        case NoopCommand() =>
      }
      
      pc += 1;
      pcUp()
  }
  
  def pcUp(): Unit = {
    while(pc < commands.length && commands.reverse(pc).isInstanceOf[NoopCommand]) pc += 1
  }
  
  def play(turtle: Turtle) {
    while(pc < commands.length) {
      executeNext(turtle)
    }
  }
  
  def lsRule(turtle: Turtle, name: String): Generator = 
    if(lsRules contains name) {
      lsRules(name)
    } else {
      new LSGenerator(turtle, name, List(name))
    }
  
  class LSGenerator(turtle: Turtle, val name: String, generates: List[String])
  extends Generator(turtle) {
    override def generate(): List[Generator]  = generates map { g => lsRule(turtle, g) }
  
    def execute() {
      returnStack ::= pc - 1
      pc = labels(name)
    }
    
    override def toString = name
  }
}