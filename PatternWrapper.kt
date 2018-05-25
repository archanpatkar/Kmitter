package com.archanpatkar.emitter;

import kotlin.text.Regex;

public class PatternWrapper
{
  val regex: Regex;
  var function: FunctionWrapper;

  constructor(pattern: Regex,function:(params:Array<Any>) -> Unit,times:Int = 1)
  {
    this.regex = pattern;
    this.function = FunctionWrapper(function,times);
  }
}
