package com.archanpatkar.test;

import com.archanpatkar.emitter.*;

fun main(args: Array<String>)
{
  println("Welcome to Kmitter");
  val em1 = EventEmitter();
  em1.on("init") { params -> params.forEach(::println) }
  em1.emit("init",10,20,30);
  em1.emit("init",100,200,300);
  em1.emit("init","Hello","Goodbye");
  println(em1.eventCount());
  em1 emit "init"
  val aem1 = AsyncEmitter();
  aem1.on("init") { params -> params.forEach(::println) }
  aem1.emit("init",10,20,30);
  aem1.emit("init","Hello World!");
}
