package ar.unq.concu.app;

import ar.unq.concu.model.Buffer;
import ar.unq.concu.model.ConcurVector;
import ar.unq.concu.model.ThreadPool;
import ar.unq.concu.model.VectorManagerResult;

public class App {

	private static final int MAX_THREADS = 10;
	private static final int CAPACITY_BUFFER = 50;
	
	public static void main( String[] args ) {
		
		System.out.println( "****CREATE OBJECTS****" );
		
		Buffer buffer = new Buffer(CAPACITY_BUFFER);
		
		VectorManagerResult vectorManagerResult = new VectorManagerResult();
		
		ThreadPool threadPool = new ThreadPool(buffer, vectorManagerResult);
		
		ConcurVector concurVector = new ConcurVector(20, MAX_THREADS, buffer, vectorManagerResult);
		
		System.out.println( "****INITIALIZE****" );
		
		threadPool.createWorkers(MAX_THREADS);
		
		threadPool.workersStart();
		
		concurVector.set(1);
		
		double result = concurVector.max();

		System.out.println( "RESULT: " + result );
		
	}
}
