package com.scurtle.script

trait Command

case class MoveCommand(expr: Expression) extends Command
case class LeftCommand(expr: Expression) extends Command
case class RightCommand(expr: Expression) extends Command
case class DrawCommand(expr: Expression) extends Command
case class PointCommand(expr: Expression) extends Command
case class HomeCommand() extends Command
case class RememberCommand() extends Command
case class GoBackCommand() extends Command
case class RepeatCommand(expr: Expression) extends Command
case class NextCommand() extends Command
case class NoopCommand() extends Command
case class LabelCommand(name: String) extends Command
case class GoCommand(label: String) extends Command
case class ReturnCommand() extends Command
case class EndCommand() extends Command
case class NamedColorCommand(color: String) extends Command
case class NumberedColorCommand(color: Int) extends Command
case class RelativeColorCommand(color: Int) extends Command
case class AbsoluteThicknessCommand(expr: Expression) extends Command
case class RelativeThicknessCommand(amount: Int) extends Command
case class AbsoluteTransparencyCommand(expr: Expression) extends Command
case class RelativeTransparencyCommand(amount: Int) extends Command
case class LetCommand(name: String, expr: Expression) extends Command
case class IfCommand(comparator: CompareOperator) extends Command
case class ElseCommand() extends Command
case class EndIfCommand() extends Command
case class PushCommand(expr: Expression) extends Command
case class PopCommand(name: String) extends Command
