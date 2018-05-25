package com.archanpatkar.emitter;

import kotlin.concurrent.*;

public class AsyncEmitter: Emitter
{
  private val _events = HashMap<String,ArrayList<(params:Array<Any>)-> Unit>>();
  private val _q = ThreadSafeQueue();
  private val _workforce:Thread;
  private var _eventCount = 0;
  private var _listenerCount = 0;

  constructor(isDaemon: Boolean = false): super()
  {
    this._workforce = thread(isDaemon = isDaemon) { this.eventLoop() }
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

  public override fun once(event:String,handler:(params:Array<Any>)-> Unit)
  {

  }

  /* private fun parallelemit(event:String,vararg params:Any)
  {
    if(this._events.containsKey(event))
    {
        val _eve = this._events.get(event);
        this._eventCount++;
        for(func in _eve!!.iterator())
        {
          TODO: Adding Support for Parallel Handler Execution using CoRoutines
          func.invoke(arrayOf(*params));
        }
    } Starting using Co-Routines in the parallel emit!
  } */

  private fun iemit(event:String,vararg params:Any)
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

  public override fun emit(event:String,vararg params:Any)
  {
    this._q.enqueue(Event(event,arrayOf(*params)));
  }

  private fun eventLoop()
  {
    while(true)
    {
      val e = this._q.dequeue();
      this.iemit(e.name,*e.params)
    }
  }
}
