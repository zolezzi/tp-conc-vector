package ar.unq.concu.model;

public class Worker extends Thread{
	
	private Buffer buffer;
	
	public Worker(Buffer buffer) {
		this.buffer = buffer;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}
	
	public void run () {
		
		int i = 0;
	
		while ( true ) {
			try {
				buffer.pop();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			i ++;
		}
	
	}

}
