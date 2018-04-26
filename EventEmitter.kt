package com.archanpatkar.emitter;

public class EventEmitter : Emitter
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
