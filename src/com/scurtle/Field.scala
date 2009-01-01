
package com.scurtle

import java.awt.{Color, Dimension, Graphics}
import scala.swing.Component

class Field(fieldSize: Int, turtle: Turtle) 
extends Component {
  minimumSize = new Dimension(2 * fieldSize, 2 * fieldSize)
  preferredSize = (2 * fieldSize, 2 * fieldSize)
  size = (2 * fieldSize, 2 * fieldSize)
  
  def squareFieldSize = Math.min(size.width, size.height)
  def offset = ((size.width / 2.0d - squareFieldSize / 2.0d).toInt, 
                (size.height / 2.0d - squareFieldSize / 2.0d).toInt)
  
  private def turtleToScreen(pos: Position): (Int, Int) = {
    (Math.min(size.getWidth - offset._1, Math.max(offset._1, (((pos.x + fieldSize) / (2.0d * fieldSize)) * squareFieldSize + offset._1))).toInt,
     Math.min(size.getHeight - offset._2, Math.max(offset._2, (((pos.y + fieldSize) / (2.0d * fieldSize)) * squareFieldSize + offset._2))).toInt)
  }
  
  override def paintComponent(g: Graphics) {
    g.setColor(new Color(128, 128, 128))
    g.fillRect(0, 0, size.width, size.height)
    g.setColor(new Color(255, 255, 255))
    g.fill3DRect(offset._1, offset._2, squareFieldSize, squareFieldSize, true)
    g.setColor(new Color(0, 0, 0))
    turtle.lines.reverse foreach { l =>
      g.setColor(l.color)
      
      val crossAngle = Math.atan((l.endPos.y - l.startPos.y) / (l.endPos.x - l.startPos.x)) - Math.Pi / 2.0d
      val startTop = turtleToScreen(Position(l.startPos.x - Math.cos(crossAngle) * l.thickness * 0.5d, l.startPos.y - Math.sin(crossAngle) * l.thickness * 0.5d))
      val startBottom = turtleToScreen(Position(l.startPos.x + Math.cos(crossAngle) * l.thickness * 0.5d, l.startPos.y + Math.sin(crossAngle) * l.thickness * 0.5d))
      val endTop = turtleToScreen(Position(l.endPos.x - Math.cos(crossAngle) * l.thickness * 0.5d, l.endPos.y - Math.sin(crossAngle) * l.thickness * 0.5d))
      val endBottom = turtleToScreen(Position(l.endPos.x + Math.cos(crossAngle) * l.thickness * 0.5d, l.endPos.y + Math.sin(crossAngle) * l.thickness * 0.5d))
      g.fillPolygon(Array(startTop._1, endTop._1, endBottom._1, startBottom._1), Array(startTop._2, endTop._2, endBottom._2, startBottom._2), 4)
    }
  }
}