package com.archanpatkar.emitter;

import kotlin.text.Regex;

public class EventEmitter : Emitter
{
  private val _events = HashMap<String,ArrayList<FunctionWrapper>>();
  private val _regex = ArrayList<PatternWrapper>();
  private var _eventCount = 0;
  private var _listenerCount = 0;
  private val _universal:String;

  public constructor()
  {
    this._universal = "*";
    this.addEvent(this._universal);
  }

  public constructor(universal:String)
  {
    this._universal = universal;
    this.addEvent(this._universal);
  }

  private fun addEvent(name:String)
  {
    this._events.put(name,ArrayList<FunctionWrapper>());
  }

  public fun eventCount(): Int
  {
    return this._eventCount;
  }

  // ---------------------------------------------------------------------------------------------------
  // EVENT HANDLERS
  // ---------------------------------------------------------------------------------------------------
  public override fun on(event:String,handler:(params:Array<Any>)-> Unit): Emitter
  {
    if(this._events.containsKey(event))
    {
      this._events.get(event)?.add(FunctionWrapper(handler,0));
      this._listenerCount++;
    }
    else
    {
      val _tal = ArrayList<FunctionWrapper>();
      _tal.add(FunctionWrapper(handler,0));
      this._events.put(event,_tal);
      this._listenerCount++;
    }
    return this;
  }

  public override fun once(event:String,handler:(params:Array<Any>)-> Unit): Emitter
  {
    if(this._events.containsKey(event))
    {
      this._events.get(event)?.add(FunctionWrapper(handler,1));
      this._listenerCount++;
    }
    else
    {
      val _tal = ArrayList<FunctionWrapper>();
      _tal.add(FunctionWrapper(handler,1));
      this._events.put(event,_tal);
      this._listenerCount++;
    }
    return this;
  }
  // ---------------------------------------------------------------------------------------------------

  // ***************************************************************************************************
  // PATTERN BASED
  // ***************************************************************************************************
  public override fun on(event:Regex,handler:(params:Array<Any>)-> Unit): Emitter
  {
    this._regex.add(PatternWrapper(event,handler,0))
    this._listenerCount++;
    return this;
  }

  public override fun once(event:Regex,handler:(params:Array<Any>)-> Unit): Emitter
  {
    this._regex.add(PatternWrapper(event,handler,1))
    this._listenerCount++;
    return this;
  }
  // ***************************************************************************************************


  public override fun emit(event:String,vararg params:Any): Emitter
  {
    if(this._events.containsKey(event))
    {
        // Before Event
        var eve = this._events.get(event);
        this._eventCount++;
        // Before Normal Handlers
        for(func in eve!!.iterator())
        {
          // Pre-Handler-Interceptors
          if(func.times == 0)
          {
            func.function.invoke(arrayOf(*params));
            func.count++;
          }
          else if(func.times == 1)
          {
            func.function.invoke(arrayOf(*params));
            eve.remove(func);
          }
          else if(func.count < func.times)
          {
            func.function.invoke(arrayOf(*params));
            func.count++;
          }
          else if(func.count == func.times)
          {
            eve.remove(func);
          }
          // Post-Handler-Interceptors
        }
        // After Normal Handlers
        // Universal Handlers are to be given the LAST PRIORITY!
        // Before Universal Handlers
        eve = this._events.get(this._universal);
        for(func in eve!!.iterator())
        {
          // Pre-Handler-Interceptors
          if(func.times == 0)
          {
            func.function.invoke(arrayOf(*params));
            func.count++;
          }
          else if(func.times == 1)
          {
            func.function.invoke(arrayOf(*params));
            eve.remove(func);
          }
          else if(func.count < func.times)
          {
            func.function.invoke(arrayOf(*params));
            func.count++;
          }
          else if(func.count == func.times)
          {
            eve.remove(func);
          }
          // Post-Handler-Interceptors
        }
        // After Universal Handlers
        // After Event
    }
    return this;
  }

  public infix fun emit(event:String)
  {
    this.emit(event,emptyArray<Any>());
  }

}
