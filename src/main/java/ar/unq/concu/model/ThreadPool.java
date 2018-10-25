package ar.unq.concu.model;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {
	
	List<Worker> workers = new ArrayList<Worker>();
	Buffer buffer;
	VectorManagerResult vectorManagerResult;
	
	public ThreadPool(Buffer buffer, VectorManagerResult vectorManagerResult) {
		this.buffer = buffer;
		this.vectorManagerResult = vectorManagerResult;
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

	public VectorManagerResult getVectorManagerResult() {
		return vectorManagerResult;
	}

	public void setVectorManagerResult(VectorManagerResult vectorManagerResult) {
		this.vectorManagerResult = vectorManagerResult;
	}
	
	public void workersStart() {
		for(Worker worker : workers) {
			System.out.println( "Start Worker");
			worker.start();
		}
	}

	public void createWorkers(int quantity) {
		
		for (int i = 0; i < quantity; i++) {

			this.workers.add(new Worker(buffer, vectorManagerResult));
		
		}
		
	}
	
	public void clearWorkers() {
		this.workers.clear();
	}

}
