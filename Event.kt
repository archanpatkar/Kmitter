package com.archanpatkar.emitter;

class Event
{
  var name:String;
  var params:Array<Any>;

  constructor(name: String,params: Array<Any>)
  {
    this.name = name;
    this.params = params;
  }
}
