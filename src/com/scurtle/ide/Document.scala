package com.scurtle.ide

import java.awt.Color
import java.io.{File, FileReader, FileWriter, BufferedReader, BufferedWriter}
import javax.swing.{ImageIcon, SwingUtilities}
import javax.swing.event.{DocumentEvent, DocumentListener}
import javax.swing.text.{DefaultStyledDocument, Style, StyleConstants}

import scala.io.Source
import scala.swing.Reactions
import scala.swing.event.Event

case class ModifiedFlagChanged(doc: Document) extends Event
case class BackingFileChanged(doc: Document) extends Event
case class TextChanged(doc: Document, modifiedLines: List[(Int, String)]) extends Event

class Document
extends DefaultStyledDocument {
  val normalStyle = addStyle("normal", null)
  StyleConstants.setForeground(normalStyle, new Color(0.0f, 0.0f, 0.0f))
  
  val errorStyle = addStyle("error", null)
  StyleConstants.setForeground(errorStyle, new Color(0.75f, 0.0f, 0.0f))
  
  val commandStyle = addStyle("command", null)
  StyleConstants.setForeground(commandStyle, new Color(0.0f, 0.0f, 0.75f))
  
  val literalStyle = addStyle("literal", null)
  StyleConstants.setForeground(literalStyle, new Color(0.0f, 0.75f, 0.0f))
  
  val highlightStyle = addStyle("highlight", null)
  StyleConstants.setBackground(highlightStyle, new Color(1.0f, 1.0f, 0.0f))
  
  val unhighlightStyle = addStyle("unhighlight", null)
  StyleConstants.setBackground(unhighlightStyle, new Color(1.0f, 1.0f, 1.0f))
  
  setLogicalStyle(0, normalStyle)
  
  val reactions: Reactions = new Reactions.Impl()
  
  def setStyle(offset: Int, length: Int, style: Style): Unit = setStyle(offset, length, style, true)
  
  def setStyle(offset: Int, length: Int, style: Style, replace: Boolean) {
    SwingUtilities.invokeLater(new Runnable() {
      override def run() {
        setCharacterAttributes(offset, length, style, replace)              
      }
    })
  }
  
  private def setModified(value: Boolean) {
    if(modified != value) {
      modified = value
      reactions(ModifiedFlagChanged(this))
    }
  }
  
  private def setBackingFile(value: Option[File]) {
    if(backingFile != value) {
      backingFile = value
      reactions(BackingFileChanged(this))
    }
  }
  
  def modifiedLinesFor(e: DocumentEvent): List[(Int, String)] =  {
    val text = getText(0, getLength)
    val beforeText = text.take(e.getOffset)
    val afterText = text.drop(e.getOffset + e.getLength)
    val relevantText = text.drop(e.getOffset).take(e.getLength)
    val lineBefore = beforeText.reverse.takeWhile(c => c != '\n').reverse
    val lineAfter = afterText.takeWhile(c => c != '\n')

    var currOffset = beforeText.length - lineBefore.length
    var lines: List[(Int, String)] = Nil
    (lineBefore ++ relevantText ++ lineAfter).mkString("").split("\n") foreach { l =>
      lines = (currOffset, l) :: lines
      currOffset += l.length + 1
    }
    lines
  }
  
  addDocumentListener(new DocumentListener {
    def changedUpdate(e: DocumentEvent) { }
    
    def insertUpdate(e: DocumentEvent) {
      setModified(true)
      reactions(TextChanged(Document.this, modifiedLinesFor(e)))
    }
    
    def removeUpdate(e: DocumentEvent) {
      setModified(true)
      reactions(TextChanged(Document.this, modifiedLinesFor(e)))
    }
  })
  
  def loadFrom(file: File) {
    setBackingFile(Some(file))
    
    val builder = new StringBuilder()
    val reader = new BufferedReader(new FileReader(file))
    var lastLine = ""
    do {
      lastLine = reader.readLine()
      if(lastLine != null) {
        builder.append(lastLine + "\n")
      }
    } while(lastLine != null)
    reader.close()
    
    replace(0, getLength, builder.toString, null)
    setModified(false)
  }
  
  def save() {
    backingFile match {
      case Some(f) => 
        val writer = new FileWriter(f)
        writer.write(getText(0, getLength), 0, getLength)
        writer.close()
        setModified(false)
      case None =>
    }
  }
  
  def reset() {
    replace(0, getLength, "", null)
    
    setBackingFile(None)
    setModified(false)
  }
  
  def getLines(): Seq[String] = getText(0, getLength).split("\n")
  
  def getLineOffset(lineNumber: Int): Int = { 
    val lines = getLines
    var offset = 0
    for(i <- 0 to (lineNumber - 1)) {
      offset += lines(i).length + 1
    }
    offset
  }
  
  private var highlightedLine: Int = 0
  
  def highlightLine(lineNum: Int) {
    removeHighlight()
    highlightedLine = lineNum
    setStyle(getLineOffset(highlightedLine), getLines()(highlightedLine).length, highlightStyle, false)
  }
  
  def removeHighlight() {
    setStyle(getLineOffset(highlightedLine), getLines()(highlightedLine).length, unhighlightStyle, false)
  }  
  var backingFile: Option[File] = None
  var modified: Boolean = false
}