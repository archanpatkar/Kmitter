package com.archanpatkar.emitter;

import kotlin.text.Regex;


interface Emitter
{
  // Normal
  fun on(event:String,handler:(params:Array<Any>)-> Unit): Emitter
  fun once(event:String,handler:(params:Array<Any>)-> Unit): Emitter

  // Regex
  fun on(event:Regex,handler:(params:Array<Any>)-> Unit): Emitter
  fun once(event:Regex,handler:(params:Array<Any>)-> Unit): Emitter

  // Broadcast
  fun emit(event:String,vararg params:Any): Emitter
}


/*
 TODO: Adding support for knowing the number of Listeners overall [Done]
 TODO: Adding support for knowing the number of Listeners and per event [Current]
 TODO: Adding support for number of times the handler was executed [Done]
 TODO: Adding support for chaining by returning this [Done]
 TODO: Adding support for Single Event Instance Handlers [Done]
 TODO: Adding support for Regular Expressions [Current]
 TODO: Adding support for Universal Handler [Current]
 TODO: Adding support for Namespaces [Next]
 TODO: Adding support for param number control per event and per handler [Next]
 TODO: Adding support for Interception between handlers and before and after the event
 TODO: Adding support for a Reactive Property named Promitter with ready hooks for
       before,changed,after in addition to interceptors and also a Async Variation
 */
