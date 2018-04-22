package com.archanpatkar.emitter;

import kotlin.concurrent.thread;

interface Emitter
{
  fun on(event:String,handler:(params:Array<Any>)-> Unit)
  fun emit(event:String,vararg params:Any)
}

class EventEmitter : Emitter
{
  val _events = HashMap<String,ArrayList<(params:Array<Any>)-> Unit>>();
  var _eventCount = 0
      private set

  public fun eventCount(): Int
  {
    return this._eventCount;
  }

  public override fun on(event:String,handler:(params:Array<Any>)-> Unit)
  {
    if(this._events.containsKey(event))
    {
      this._events.get(event)?.add(handler);
    }
    else
    {
      val _tal = ArrayList<(params:Array<Any>)-> Unit>();
      _tal.add(handler);
      this._events.put(event,_tal);
    }
  }

  public override fun emit(event:String,vararg params:Any)
  {
    if(this._events.containsKey(event))
    {
        val eve = this._events.get(event);
        this._eventCount++;
        for(func in eve!!.iterator())
        {
          func.invoke(arrayOf(*params));
        }
    }
  }
}

class ThreadSafeQueue;

class AsyncEmitter: Emitter
{
  private val _events = HashMap<String,ArrayList<(params:Array<Any>)-> Unit>>();
  private val _q:ThreadSafeQueue? = null;
  private var _workforce:Thread? = null;
  private var _eventCount = 0;

  constructor(isDaemon: Boolean = false): super()
  {
    this._workforce = thread(true,isDaemon) { this.eventLoop(); }
  }

  public fun eventCount(): Int
  {
    return this._eventCount;
  }

  public override fun on(event:String,handler:(params:Array<Any>)-> Unit)
  {
    if(this._events.containsKey(event))
    {
      this._events.get(event)?.add(handler);
    }
    else
    {
      val _tal = ArrayList<(params:Array<Any>)-> Unit>();
      _tal.add(handler);
      this._events.put(event,_tal);
    }
  }

  public override fun emit(event:String,vararg params:Any)
  {
    if(this._events.containsKey(event))
    {
        val eve = this._events.get(event);
        this._eventCount++;
        for(func in eve!!.iterator())
        {
          func.invoke(arrayOf(*params));
        }
    }
  }

  private fun eventLoop(){}

}

fun main(args: Array<String>)
{
  println("Welcome to Kmitter");
  val em1 = EventEmitter();
  em1.on("init") { params -> params.forEach(::println) }
  em1.emit("init",10,20,30);
  em1.emit("init",100,200,300);
  em1.emit("init","Hello","Goodbye");
  println(em1.eventCount());
}
