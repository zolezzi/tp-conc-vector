package ar.unq.concu.model;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {
	
	List<Worker> workers = new ArrayList<Worker>();
	Buffer buffer;
	
	public ThreadPool(Buffer buffer) {
		this.buffer = buffer;
	}
	
	public List<Worker> getWorkers() {
		return workers;
	}

	public void setWorkers(List<Worker> workers) {
		this.workers = workers;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}

	public void createWorkers(int quantity) {
		
		clearWorkers();
		
		for (int i = 0; i < quantity; i++) {
			this.workers.add(new Worker(buffer));
		}
		
	}
	
	public void clearWorkers() {
		this.workers.clear();;
	}

}
