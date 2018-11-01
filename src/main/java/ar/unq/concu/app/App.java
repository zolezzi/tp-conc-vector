package ar.unq.concu.app;

import java.util.Random;

import ar.unq.concu.model.Buffer;
import ar.unq.concu.model.ConcurVector;
import ar.unq.concu.model.ThreadPool;
import ar.unq.concu.model.VectorManagerResult;

public class App {

	private static final int MAX_THREADS = 5;
	private static final int CAPACITY_BUFFER = 50;
	private static double [] elements = new double[20];
	
	public static void main( String[] args ) {
		
		Random rand = new Random();
		
		for (int i = 0; i < 19; i++) {
			int  n = rand.nextInt(50) + 1;
			elements[i] = n;
		}
		
		System.out.println( "****CREATE OBJECTS****" );
		
		Buffer buffer = new Buffer(CAPACITY_BUFFER);
		
		VectorManagerResult vectorManagerResult = new VectorManagerResult();
		
		ThreadPool threadPool = new ThreadPool(buffer, vectorManagerResult);
		
		ConcurVector concurVector = new ConcurVector(20, MAX_THREADS, buffer, vectorManagerResult);
		
		System.out.println( "****INITIALIZE****" );
		
		threadPool.createWorkers(MAX_THREADS);
		
		threadPool.workersStart();
		
		System.out.println( "ELEMENTS:" );
		for (int i = 0; i < 19; i++) {
			
			concurVector.setWithPosition(i,elements[i]);
			System.out.println( "elemento value: "+ elements[i] );
		}
		
		double result = concurVector.max();

		System.out.println( "RESULT: " + result );
		
	}
}
