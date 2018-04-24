package com.archanpatkar.emitter;

import kotlin.concurrent.*;
import kotlinx.coroutines.experimental.*;


interface Emitter
{
  fun on(event:String,handler:(params:Array<Any>)-> Unit)
  fun emit(event:String,vararg params:Any)
}

class EventEmitter : Emitter
{
  private val _events = HashMap<String,ArrayList<(params:Array<Any>)-> Unit>>();
  private var _eventCount = 0

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

  private fun semit(event:String)
  {
    if(this._events.containsKey(event))
    {
        val eve = this._events.get(event);
        this._eventCount++;
        for(func in eve!!.iterator())
        {
          func.invoke(emptyArray<Any>());
        }
    }
  }

  public infix fun emit(event:String)
  {
    this.semit(event);
  }

}


class Event(name: String,params: Array<Any>);


class ThreadSafeQueue
{
  val _ds = ArrayList<Event>();
  val _lock = java.lang.Object();

  @Synchronized fun enqueue(event: Event)
  {
    this._ds.add(event);
    this._lock.notify();
  }

  @Synchronized fun dequeue(): Event?
  {
    if(this._ds.get(this._ds.size - 1) == null)
    {
      this._lock.wait();
    }
    val _te = this._ds.get(this._ds.size - 1);
    this._ds.removeAt(this._ds.size - 1);
    return _te;
  }

}

class AsyncEmitter: Emitter
{
  private val _events = HashMap<String,ArrayList<(params:Array<Any>)-> Unit>>();
  private val _q = ThreadSafeQueue();
  private var _workforce:Thread? = null;
  private var _eventCount = 0;

  constructor(isDaemon: Boolean = false): super()
  {
    this._workforce = thread(start = true,isDaemon = isDaemon) { this.eventLoop(); }
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

  public fun pemit(event:String,vararg params:Any)
  {
    if(this._events.containsKey(event))
    {
        val _eve = this._events.get(event);
        this._eventCount++;
        for(func in _eve!!.iterator())
        {
          func.invoke(arrayOf(*params));
        }
    }
  }

  public override fun emit(event:String,vararg params:Any)
  {
    if(this._events.containsKey(event))
    {
        val _eve = this._events.get(event);
        this._eventCount++;
        for(func in _eve!!.iterator())
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
  em1 emit "init"
}
