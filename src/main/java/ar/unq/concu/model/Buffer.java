package ar.unq.concu.model;

import java.util.Arrays;

public class Buffer {
	
	private int capacity;
	
	public VectorTasks [] data;
	
	private int begin = 0, end = 0;
	
	public Buffer(int capacity) {
		this.capacity = capacity;
		this.data = new VectorTasks [capacity]; 
	}
	
	public boolean getVectorTasksIsEmpty() {
		
		boolean isEmpty = true;
		
		for(VectorTasks vectorTasks : Arrays.asList(data)) {
			isEmpty = isEmpty && vectorTasks == null;
		}
		
		return isEmpty;
	}
	
	public synchronized void push (VectorTasks vectorTaks) throws InterruptedException {
		
		while (isFull()) wait();
		
		data[begin] = vectorTaks;
		
		begin = next(begin);
		
		notifyAll();
	
	}
	
	public synchronized VectorTasks pop () throws InterruptedException {
		
		while (isEmpty()) wait();
		
		VectorTasks result = data[end];
		
		data[end] = null;
	
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
	
		return (i +1) %( capacity); 
	}

}
