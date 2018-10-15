package ar.unq.concu.model;

class Buffer {
	
	private int capacity;
	
	private Object [] data = new Object [capacity +1];
	
	private int begin = 0, end = 0;
	
	public Buffer() {
		
	}
	
	public synchronized void push ( Object o) throws InterruptedException {
		
		while ( isFull ()) wait ();
		
		data [ begin ] = o;
		
		begin = next ( begin );
		
		notifyAll ();
	
	}
	
	public synchronized Object pop () throws InterruptedException {
		
		while (isEmpty()) wait();
		
		Object result = data [ end ];
		
		end = next ( end );
		
		notifyAll ();
		
		return result ;
	
	}
	
	private boolean isEmpty () { return begin == end ; }
	
	private boolean isFull () { return next ( begin ) == end ; }
	
	private int next ( int i) { return (i +1) %( capacity +1); }

}
