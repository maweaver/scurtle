package com.scurtle.ide

import java.util.regex.{Matcher, Pattern}
import javax.swing.{Action, JTextPane, SwingUtilities}
import javax.swing.text.{Style}
import scala.swing.{Component, ScrollPane}
import scala.util.parsing.combinator.Parsers
import com.scurtle.script.{TurtleMachine, TurtleParser}

class ScurtleEditor(frame: ScurtleIdeFrame) 
extends ScrollPane {
  
  val panel = new JTextPane()
  panel.setDocument(frame.document)
  
  val parser = new TurtleParser()
  
  val commandsPattern = Pattern.compile("(move|left|right|draw|point|home|remember|goback|repeat|next|#|go|return|end|color|thick|transparent|\\+|-|/|\\*|\\?|let|if|else|endif|push|pop)", Pattern.CASE_INSENSITIVE)
  val literalsPattern = Pattern.compile("[0-9]+")
  val commentPattern = Pattern.compile(";.*")
  
  frame.document.reactions += {
    case TextChanged(doc, lines) =>
      lines foreach { l =>
        val parseResult = parser.parseLine(l._2)
        if(parseResult.successful) {
          val commentMatcher = commentPattern.matcher(l._2)
          val commentFound = commentMatcher.find()

          var currentPos = l._1
          val lineEndPos = if(commentFound) commentMatcher.start else l._1 + l._2.length
          val commandsMatcher = commandsPattern.matcher(l._2)
          val literalsMatcher = literalsPattern.matcher(l._2)
          
          def lineToGlobal(offset: Int): Int = {
            offset + l._1
          }
          
          var commandFound = commandsMatcher.find()
          var literalFound = literalsMatcher.find() 
          
          do {
            val nextCommandStart = if(commandFound) lineToGlobal(commandsMatcher.start) else Int.MaxValue
            val nextCommandLen = if(commandFound) lineToGlobal(commandsMatcher.end) - nextCommandStart else 0
            
            val nextLiteralStart = if(literalFound) lineToGlobal(literalsMatcher.start) else Int.MaxValue
            val nextLiteralLen = if(literalFound) lineToGlobal(literalsMatcher.end) - nextLiteralStart else 0
            
            val nextStyle = 
              if(nextCommandStart == currentPos) {
                commandFound = commandsMatcher.find()
                (doc.commandStyle, nextCommandLen)
              } else if(nextLiteralStart == currentPos) {
                literalFound = literalsMatcher.find()
                (doc.literalStyle, nextLiteralLen)
              } else {
                (doc.normalStyle, Math.min(lineEndPos, Math.min(nextCommandStart, nextLiteralStart)) - currentPos)
              }
            doc.setStyle(currentPos, nextStyle._2, nextStyle._1)
            currentPos += nextStyle._2
          } while(currentPos < lineEndPos)
          
            if(commentFound) {
              val commentStart = lineToGlobal(commentMatcher.start)
              val commentEnd = lineToGlobal(commentMatcher.end)
              doc.setStyle(commentStart, commentEnd - commentStart, doc.normalStyle)
            }
        } else {
          doc.setStyle(l._1, l._2.length, doc.errorStyle)
        }
      }
      
      frame.setHasErrors(doc.getLines.foldLeft(false) { (errors, l) => errors || !parser.parseLine(l).successful })
  }
  
  def createTurtleMachine(): TurtleMachine = {
    val turtleMachine = new TurtleMachine()
    val parser = new TurtleParser()
    frame.document.getLines foreach { l => 
      val command = parser.parseLine(l)
      turtleMachine.addCommand(command.get)
    }
    
    turtleMachine.pcUp()
    turtleMachine
  }
  
  private var mEditable: Boolean = true
  def editable: Boolean = mEditable
  def editable_=(value: Boolean) { mEditable = value; panel.setEditable(value) }
  
  def cut() = panel.cut()
  def copy() = panel.copy()
  def paste() = panel.paste()
  def delete() = panel.replaceSelection("")
  def selectAll() = panel.selectAll()
  
  contents = Component.wrap(panel)
}
