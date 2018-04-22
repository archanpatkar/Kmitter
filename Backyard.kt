/* interface Handler
{
  fun handle(vararg params:Any)
} */
/* println(em1._events.get("init")?.get(0)?.invoke(arrayOf("January", "February", "March"))); */

class Event(name: String , params: Array<Any>)
{

}


class ThreadSafeQueue
{
  val _ds = ArrayList<Event>();

  @Synchronized fun enqueue(event: Event)
  {

  }

  @Synchronized fun dequeue(): Event?
  {
    if(this._ds.get(this._ds.size - 1) == null)
    {
      wait();
    }
    val _te = this._ds.get(this._ds.size - 1);
    this._ds.removeAt(this._ds.size - 1);
    return _te;
  }

}

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
