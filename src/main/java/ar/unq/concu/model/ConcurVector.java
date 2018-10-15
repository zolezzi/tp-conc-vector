package ar.unq.concu.model;

public class ConcurVector extends SeqVector{
		
	private int totalThread;
	
	public ConcurVector(int dimension, int totalThread) {
		super(dimension);
		this.totalThread = totalThread;
	}
	
	
	
}
