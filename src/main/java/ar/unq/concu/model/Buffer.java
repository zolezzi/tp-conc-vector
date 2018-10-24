package ar.unq.concu.model;

public class Buffer {
	
	private int capacity;
	
	private VectorTaks [] data = new VectorTaks [capacity +1];
	
	private int begin = 0, end = 0;
	
	public Buffer(int capacity) {
		this.capacity = capacity;
	}
	
	public synchronized void push ( VectorTaks vectorTaks) throws InterruptedException {
		
		while (isFull()) wait();
		
		data[begin] = vectorTaks;
		
		begin = next(begin);
		
		notifyAll();
	
	}
	
	public synchronized VectorTaks pop () throws InterruptedException {
		
		while (isEmpty()) wait();
		
		VectorTaks result = data[end];
		
		end = next(end);
		
		notifyAll();
		
		return result ;
	
	}
	
	private boolean isEmpty() { 
	
		return begin == end ; 
	}
	
	private boolean isFull() { 
		return next (begin) == end; 
	}
	
	private int next (int i) { 
	
		return (i +1) %( capacity +1); 
	}

}
