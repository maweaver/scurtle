package com.scurtle.script

case class Variable(name: String, value: Int)

trait Expression {
  def eval(variables: List[Variable]): Int
}

case class ConstantExpression(value: Int) 
extends Expression {
  override def eval(variables: List[Variable]) = value
}

case class VariableExpression(name: String) 
extends Expression {
  override def eval(variables: List[Variable]) = (variables filter(v => v.name == name)).head.value
}

case class PlusExpression(left: Expression, right: Expression) 
extends Expression {
  override def eval(variables: List[Variable]) = left.eval(variables) + right.eval(variables)
}

case class MinusExpression(left: Expression, right: Expression) 
extends Expression {
  override def eval(variables: List[Variable]) = left.eval(variables) - right.eval(variables)
}

case class DivExpression(left: Expression, right: Expression) 
extends Expression {
  override def eval(variables: List[Variable]) = left.eval(variables) / right.eval(variables)
}

case class MultExpression(left: Expression, right: Expression) 
extends Expression {
  override def eval(variables: List[Variable]) = left.eval(variables) * right.eval(variables)
}

case class RandExpression(left: Expression, right: Expression) 
extends Expression {
  override def eval(variables: List[Variable]) = {
    val min = left.eval(variables)
    val max = right.eval(variables)
    (Math.random * (max - min)).toInt + min
  }
}