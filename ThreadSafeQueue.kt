package com.archanpatkar.emitter;

public class ThreadSafeQueue
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
