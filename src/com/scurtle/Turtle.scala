package com.scurtle

import java.awt.Color

case class Position(x: Double, y: Double)
case class Line(startPos: Position, endPos: Position, thickness: Int, color: Color)

class Turtle {
  var pos: Position = _
  var color: Color = _
  var thickness: Int = _
  var alpha: Float = _
  var rotation: Double = _
  def rotationDegrees: Int = 
    (((rotation + Math.Pi / 2.0d) / (2.0d * Math.Pi)) * 360d).toInt % 360
  var lines: List[Line] = Nil
  
  reset()
  
  def move(distance: Double) {
    pos = Position(pos.x + Math.cos(rotation) * distance, pos.y + Math.sin(rotation) * distance)
  }
  
  def rotate(deltaRotation: Double) {
    rotation += toRadians(deltaRotation)
  }
  
  def draw(distance: Double) {
    val startPos = pos
    move(distance)
    lines ::= Line(startPos, pos, thickness, new Color(color.getRed(), color.getGreen(), color.getBlue(), (alpha * 255).toInt))
  }
  
  def point(degree: Double) {
    rotation = toRadians(degree) - Math.Pi / 2.0d
  }
  
  def home() {
    pos = Position(0.0d, 0.0d)
    rotation = -Math.Pi / 2.0d
  }
  
  def reset() {
    home()
    lines = Nil
    thickness = 1
    alpha = 1.0f
    color = Color.black
  }
  
  def currentState: (Position, Double) = (pos, rotation)
  def currentState_=(state: (Position, Double)) = { pos = state._1; rotation = state._2 }
  
  private def toRadians(degree: Double) = degree / 360.0d * 2.0d * Math.Pi
}