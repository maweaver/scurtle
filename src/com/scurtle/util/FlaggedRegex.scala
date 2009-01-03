package com.scurtle.util

import java.util.regex.{Pattern}
import scala.util.matching.{Regex}

class FlaggedRegex(regex: String, flags: Int) 
extends Regex(regex) {
  override val pattern = Pattern.compile(regex, flags)
}

object FlaggedRegex {
  val UnixLines: Int       =   1
  val CaseInsensitive: Int =   2
  val Comments: Int        =   4
  val MultiLine: Int       =   8
  val Literal: Int         =  16
  val DotAll: Int          =  32
  val UnicodeCase: Int     =  64
  val CanonEq: Int         = 128
}

class FlaggedRegexString(value: String) {
  def rf(flags: Int): FlaggedRegex = new FlaggedRegex(value, flags)
}

object FlaggedRegexString {
  implicit def stringToFlaggedRegexString(value: String): FlaggedRegexString =
    new FlaggedRegexString(value)
}