package com.scurtle.script

import scala.util.parsing.combinator._
import FlaggedRegex._
import FlaggedRegexString._

class TurtleParser 
extends JavaTokenParsers {
  
  def parseLine(l: String): ParseResult[Command] = parseAll(line, l)
  
  def line: Parser[Command] = ""~>opt(command)<~opt(comment) ^^ { c => c match {
    case Some(command) => command
    case None => NoopCommand() } }
  
  def comment: Parser[Any] = ";.*".r
  def command: Parser[Command] = 
    move|left|right|draw|point|
    home|remember|goback|
    repeat|next|
    label|go|returnCommand|
    namedColor|numberedColor|relativeColor|
    absoluteThickness|relativeThickness|
    absoluteTransparency|relativeTransparency|
    let|
    ifCmd|elseCmd|endIf|
    push|pop|
    end
  
  def move: Parser[MoveCommand] = "move".rf(CaseInsensitive)~>expression<~"" ^^ { case d => MoveCommand(d) }
  
  def left: Parser[LeftCommand] = "left".rf(CaseInsensitive)~>expression<~"" ^^ { case d => LeftCommand(d) }
  
  def right: Parser[RightCommand] = "right".rf(CaseInsensitive)~>expression<~"" ^^ { case d => RightCommand(d) }
  
  def draw: Parser[DrawCommand] = "draw".rf(CaseInsensitive)~>expression<~"" ^^ { case d => DrawCommand(d) }
  
  def point: Parser[PointCommand] = "point".rf(CaseInsensitive)~>expression<~"" ^^ { case d => PointCommand(d) }
  
  def home: Parser[HomeCommand] = "home".rf(CaseInsensitive) ^^ { s => HomeCommand() }
  
  def remember: Parser[RememberCommand] = "remember".rf(CaseInsensitive) ^^ { s => RememberCommand() }
  
  def goback: Parser[GoBackCommand] = "goback".rf(CaseInsensitive) ^^ { s => GoBackCommand() }
  
  def repeat: Parser[RepeatCommand] = "repeat".rf(CaseInsensitive)~>expression<~"" ^^ { case d => RepeatCommand(d)}
  
  def next: Parser[NextCommand] = "next".rf(CaseInsensitive) ^^ { s => NextCommand() }
  
  def label: Parser[LabelCommand] = "#"~>ident<~"" ^^ { s => LabelCommand(s) }
  
  def go: Parser[GoCommand] = "go".rf(CaseInsensitive)~>ident<~"" ^^ { s => GoCommand(s) }
  
  def returnCommand: Parser[ReturnCommand] = "return".rf(CaseInsensitive) ^^ { s => ReturnCommand() }
  
  def end: Parser[EndCommand] = "end".rf(CaseInsensitive) ^^ { s => EndCommand() }
  
  def namedColor: Parser[NamedColorCommand] = "color".rf(CaseInsensitive)~>ident<~"" ^^ { s => NamedColorCommand(s) }
  
  def numberedColor: Parser[NumberedColorCommand] = "color".rf(CaseInsensitive)~>"[0-9]".r<~"" ^^ { s => NumberedColorCommand(s.toInt) }
  
  def relativeColor: Parser[RelativeColorCommand] = "color".rf(CaseInsensitive)~opt("+")~>"-?[0-9]".r<~"" ^^ { s => RelativeColorCommand(s.toInt) }
  
  def absoluteThickness: Parser[AbsoluteThicknessCommand] = "thick".rf(CaseInsensitive)~>expression<~"" ^^ { s => AbsoluteThicknessCommand(s) }
  
  def relativeThickness: Parser[RelativeThicknessCommand] = "thick".rf(CaseInsensitive)~opt("+")~>"-?[0-9]+".r<~"" ^^ { s => RelativeThicknessCommand(s.toInt) }
  
  def absoluteTransparency: Parser[AbsoluteTransparencyCommand] = "transparent".rf(CaseInsensitive)~>expression<~"" ^^ { s => AbsoluteTransparencyCommand(s) }
  
  def relativeTransparency: Parser[RelativeTransparencyCommand] = "transparent".rf(CaseInsensitive)~opt("+")~>"-?[0-9]+".r<~"" ^^ { s => RelativeTransparencyCommand(s.toInt) }
  
  def let: Parser[LetCommand] = "let".rf(CaseInsensitive)~>ident~expression<~"" ^^ { rhs => LetCommand(rhs._1, rhs._2) }
  
  def ifCmd: Parser[IfCommand] = "if".rf(CaseInsensitive)~>(equal|inequal|lessThan|lessThanEq|greaterThan|greaterThanEq)<~"" ^^ { case a => IfCommand(a) }
  
  def elseCmd: Parser[ElseCommand] = "else".rf(CaseInsensitive) ^^ { case a => ElseCommand() }
  
  def endIf: Parser[EndIfCommand] = "endif".rf(CaseInsensitive) ^^ { case a => EndIfCommand() }
  
  def push: Parser[PushCommand] = "push".rf(CaseInsensitive)~>expression<~"" ^^ { rhs => PushCommand(rhs) }
  
  def pop: Parser[PopCommand] = "pop".rf(CaseInsensitive)~>ident<~"" ^^ { rhs => PopCommand(rhs) }
  
  def term: Parser[Expression] = (variable|constant)
  
  def expression: Parser[Expression] = chainl1(term, (plusOp|minusOp|divOp|multOp|randOp)) 
  
  def variable: Parser[VariableExpression] = ident ^^ { s => VariableExpression(s) }
  
  def constant: Parser[ConstantExpression] = wholeNumber ^^ { s => ConstantExpression(s.toInt) }
  
  def plusOp: Parser[(Expression, Expression) => PlusExpression] = "+" ^^ { case a => (left: Expression, right: Expression) => PlusExpression(left, right) }
  
  def minusOp: Parser[(Expression, Expression) => MinusExpression] = "-" ^^ { case a => (left: Expression, right: Expression) => MinusExpression(left, right) }
                                                                            
  def divOp: Parser[(Expression, Expression) => DivExpression] = "/" ^^ { case a => (left: Expression, right: Expression) => DivExpression(left, right) }                                                                            
  
  def multOp: Parser[(Expression, Expression) => MultExpression] = "*" ^^ { case a => (left: Expression, right: Expression) => MultExpression(left, right) }
  
  def randOp: Parser[(Expression, Expression) => RandExpression] = "?" ^^ { case a => (left: Expression, right: Expression) => RandExpression(left, right) }
  
  def equal: Parser[EqualityOperator] = expression~"==?".r~expression ^^ { rhs => EqualityOperator(rhs._1._1, rhs._2) }
  
  def inequal: Parser[InequalityOperator] = expression~"(!=|<>)".r~expression ^^ { rhs => InequalityOperator(rhs._1._1, rhs._2)}

  def lessThan: Parser[LessThanOperator] = expression~"<".r~expression ^^ { rhs => LessThanOperator(rhs._1._1, rhs._2)}

  def lessThanEq: Parser[LessThanEqOperator] = expression~"<=".r~expression ^^ { rhs => LessThanEqOperator(rhs._1._1, rhs._2)}

  def greaterThan: Parser[GreaterThanOperator] = expression~">".r~expression ^^ { rhs => GreaterThanOperator(rhs._1._1, rhs._2)}

  def greaterThanEq: Parser[GreaterThanEqOperator] = expression~">=".r~expression ^^ { rhs => GreaterThanEqOperator(rhs._1._1, rhs._2)}
}