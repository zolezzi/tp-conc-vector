package ar.unq.concu.model;

public class Worker extends Thread{
	
	private Buffer buffer;
	private SeqVector seqVector;
	
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
	
		while ( true ) {
			try {
			
				VectorTaks taks = buffer.pop();
			
				//realiza esa tarea
				
				//genera un resuktado
				
				//lo almacena
			
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	
	}

}
