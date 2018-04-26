package com.archanpatkar.emitter;

import kotlin.concurrent.*;

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


class ThreadSafeQueue
{
  val _ds = ArrayList<Event>();
  val _lock = java.lang.Object();

  fun enqueue(event: Event)
  {
    synchronized(_lock)
    {
      this._ds.add(event);
      this._lock.notify();
    }
  }

  fun dequeue(): Event
  {
    synchronized(_lock)
    {
      if(this._ds.size == 0)
      {
        this._lock.wait();
      }
      val _te:Event = this._ds.get(0);
      this._ds.removeAt(0);
      return _te;
    }
  }

}

class AsyncEmitter: Emitter
{
  private val _events = HashMap<String,ArrayList<(params:Array<Any>)-> Unit>>();
  private val _q = ThreadSafeQueue();
  private val _workforce:Thread;
  private var _eventCount = 0;

  constructor(isDaemon: Boolean = false): super()
  {
    this._workforce = thread(isDaemon = isDaemon) { println("Starting the Thread"); this.eventLoop(); }
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

  private fun pemit(event:String,vararg params:Any)
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
    this._q.enqueue(Event(event,arrayOf(*params)));
  }

  private fun eventLoop()
  {
    while(true)
    {
      val e = this._q.dequeue();
      if(this._events.containsKey(e.name))
      {
          val eve = this._events.get(e.name);
          this._eventCount++;
          for(func in eve!!.iterator())
          {
            func.invoke(arrayOf(*e.params));
          }
      }
    }
  }
}

fun main(args: Array<String>)
{
  println("Welcome to Kmitter");
  /* val em1 = EventEmitter();
  em1.on("init") { params -> params.forEach(::println) }
  em1.emit("init",10,20,30);
  em1.emit("init",100,200,300);
  em1.emit("init","Hello","Goodbye");
  println(em1.eventCount());
  em1 emit "init" */
  val aem1 = AsyncEmitter();
  aem1.on("init") { params -> params.forEach(::println) }
  aem1.emit("init",10,20,30);
  aem1.emit("init","Hello World!");
  println("After AsyncEmitter");
}
