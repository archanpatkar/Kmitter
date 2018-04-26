package com.archanpatkar.emitter;

interface Emitter
{
  fun on(event:String,handler:(params:Array<Any>)-> Unit)
  fun emit(event:String,vararg params:Any)
}
