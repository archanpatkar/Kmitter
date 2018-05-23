package com.archanpatkar.emitter;

public class FunctionWrapper
{
  val function: (params:Array<Any>) -> Unit;
  val times: Int;

  constructor(function:(params:Array<Any>) -> Unit,times:Int = 1)
  {
    this.function = function;
    this.times = times;
  }
}
