package com.archanpatkar.emitter;

import kotlin.text.Regex;

public class PatternWrapper: FunctionWrapper
{
  val regex: Regex;

  constructor(pattern: Regex,function:(params:Array<Any>) -> Unit,times:Int = 1): super(function,times)
  {
    this.regex = pattern;
  }
}
