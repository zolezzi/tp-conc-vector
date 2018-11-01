package ar.unq.concu.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ar.unq.concu.model.Buffer;
import ar.unq.concu.model.ConcurVector;
import ar.unq.concu.model.ThreadPool;
import ar.unq.concu.model.VectorManagerResult;
import junit.framework.TestCase;

public class ConcurVectorTest{
	
	private static final int MAX_THREADS = 5;
	private static final int CAPACITY_BUFFER = 50;
	private static double [] elements = new double[20];
	ConcurVector concurVector;
	ThreadPool threadPool;
	VectorManagerResult vectorManagerResult;
	Buffer buffer;
	
	@Before
	public void setUp(){
		
		System.out.println( "****CREATE OBJECTS****" );
		
		buffer = new Buffer(CAPACITY_BUFFER);
		
		vectorManagerResult = new VectorManagerResult();
		
		threadPool = new ThreadPool(buffer, vectorManagerResult);
		
		concurVector = new ConcurVector(20, MAX_THREADS, buffer, vectorManagerResult);
		
		System.out.println( "****INITIALIZE****" );
		
		threadPool.createWorkers(MAX_THREADS);
		
		threadPool.workersStart();

	}
	
	@Test
	public void testConcurVectorWithExecuteMethodSetWithAVectorThe20Elements() {
		
		this.concurVector.set(2);
		
		double expected = this.concurVector.getElements()[0];
		
		System.out.println( "ELEMENTS:" + expected );
		
		assertTrue(expected == 2d);
		
	}

}
