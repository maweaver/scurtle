package com.scurtle

import script.{TurtleMachine, TurtleParser}
import util.FlaggedRegex._
import util.FlaggedRegexString._

import scala.swing.MainFrame

import java.awt.image.BufferedImage;

trait ScurtleApplication {
  val turtle = new Turtle()
  var field: Field = _
  val machine = new TurtleMachine()
  val parser = new TurtleParser()
    
  val window = new MainFrame()
  
  def parseArgs(args: Array[String]): Map[String, String] = {
    var argsMap = Map[String, String]()
    var i = 0;
    while(i < args.length) {
      if(args(i).startsWith("-")) {
        if(i < args.length - 1) {
          argsMap += args(i).replaceAll("-", "") -> args(i + 1)
          i += 1
        }
      } else {
        argsMap += "inputFile" -> args(i);
      }
      i += 1
    }
    argsMap
  }
  
  def main(args: Array[String]) {
    val argsMap = parseArgs(args)

    val fieldSize = argsMap.getOrElse("fieldSize", "150").toInt
    val windowSize = argsMap.getOrElse("windowSize", "300").toInt
    val outputFile = argsMap.getOrElse("outputFile", null)
    val display = 
      if(argsMap contains "display") {
        argsMap.get("display").get.toBoolean
      } else {
        if(outputFile != null) false else true
      }
    
    field = new Field(fieldSize, windowSize, turtle)
    window.contents = field
    
    if(exec(argsMap)) {
      field.repaint()
      if(outputFile != null) {
        field.saveTo(outputFile)
      }

      if(display) {
        window.visible = true
      } else {
        System.exit(1)
      }
    } else {
      System.exit(1)
    }
    
  }
  
  def exec(argsMap: Map[String, String]): Boolean
}
