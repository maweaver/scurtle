package com.scurtle.ide

import scala.swing.{Table}
import javax.swing.table.{AbstractTableModel}

class DebugInfo(frame: ScurtleIdeFrame) 
extends Table() {
  preferredSize = (300, Int.MaxValue)
  
  val concreteModel = new AbstractTableModel() {
    def getRowCount(): Int = 7 + (if(frame.debuggingTurtleMachine != null) frame.debuggingTurtleMachine.variables.length else 0)
    
    def getColumnCount(): Int = 2
    
    def getValueAt(row: Int, column: Int): Object = {
      row match {
        case 0 => column match {
          case 0 => "Turtle Position"
          case 1 => "(" + frame.turtle.pos.x.toInt + ", " + frame.turtle.pos.y.toInt + ")"
        }
        case 1 => column match {
          case 0 => "Turtle Rotation"
          case 1 => frame.turtle.rotationDegrees.toString
        }
        case 2 => column match {
          case 0 => "Turtle Color"
          case 1 => "(" + frame.turtle.color.getRed + ", " + frame.turtle.color.getGreen + ", " + frame.turtle.color.getBlue + ")"
        }
        case 3 => column match {
          case 0 => "Turtle Transparency"
          case 1 => ((1.0f - frame.turtle.alpha) * 100f).toInt.toString
        }
        case 4 => column match {
          case 0 => "Turtle Thickness"
          case 1 => frame.turtle.thickness.toString
        }
        case 5 => column match {
          case 0 => "Stack"
          case 1 => if(frame.debuggingTurtleMachine != null) {
            frame.debuggingTurtleMachine.stack.mkString("[", ", ", "]")
          } else {
            ""
          }
        }
        case 6 => column match {
          case 0 => "LSystem"
          case 1 => if(frame.debuggingTurtleMachine != null) {
            frame.debuggingTurtleMachine.lsystem match {
              case None => ""
              case Some(ls) => ls.toString
            }
          } else ""
        }
        case x if(x - 7 >= 0 && x - 7 < frame.debuggingTurtleMachine.variables.length) => column match {
          case 0 => frame.debuggingTurtleMachine.variables.reverse(x - 7).name
          case 1 => frame.debuggingTurtleMachine.variables.reverse(x - 7).value.toString
        } 
          
      }
    }
    
    override def getColumnName(col: Int): String = col match {
      case 0 => "Field"
      case 1 => "Value"
    }
  }
  model = concreteModel
  
  def turtleUpdated() {
    concreteModel.fireTableRowsUpdated(0, concreteModel.getRowCount)
  }
  
}