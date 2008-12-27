package com.scurtle.script

trait Operator

case class PlusOperator() extends Operator
case class MinusOperator() extends Operator
case class DivisionOperator() extends Operator
case class MultiplicationOperator() extends Operator
case class RandomOperator() extends Operator

trait CompareOperator extends Operator {
  def eval(variables: List[Variable]): Boolean
}

case class EqualityOperator(left: Expression, right: Expression) extends CompareOperator {
  def eval(variables: List[Variable]): Boolean = left.eval(variables) == right.eval(variables)
}

case class InequalityOperator(left: Expression, right: Expression) extends CompareOperator {
  def eval(variables: List[Variable]): Boolean = left.eval(variables) != right.eval(variables)
}

case class LessThanOperator(left: Expression, right: Expression) extends CompareOperator {
  def eval(variables: List[Variable]): Boolean = left.eval(variables) < right.eval(variables)
}

case class LessThanEqOperator(left: Expression, right: Expression) extends CompareOperator {
  def eval(variables: List[Variable]): Boolean = left.eval(variables) <= right.eval(variables)
}

case class GreaterThanOperator(left: Expression, right: Expression) extends CompareOperator {
  def eval(variables: List[Variable]): Boolean = left.eval(variables) > right.eval(variables)
}

case class GreaterThanEqOperator(left: Expression, right: Expression) extends CompareOperator {
  def eval(variables: List[Variable]): Boolean = left.eval(variables) >= right.eval(variables)
}