package ar.unq.concu.model;

import java.lang.reflect.Method;
import java.util.List;

public class Worker extends Thread{
	
	private int id;
	private Buffer buffer;
	private VectorManagerResult vectorManagerResult;
	private SeqVector seqVector;
	private Method[] methods;
	
	public Worker(int id, Buffer buffer, VectorManagerResult vectorManagerResult) {
		
		this.id = id;
		this.buffer = buffer;
		this.vectorManagerResult = vectorManagerResult;
	
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public VectorManagerResult getVectorManagerResult() {
		return vectorManagerResult;
	}

	public void setVectorManagerResult(VectorManagerResult vectorManagerResult) {
		this.vectorManagerResult = vectorManagerResult;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}
	
	private void loadValues(SeqVector seqVector, List<Double> values) {
		for (int i = 0; i < values.size(); i++) {
			seqVector.setWithPosition(i,values.get(i));
		}
	}
	
	private Method getMethod(String OperationName){
		
		methods = seqVector.getClass().getMethods();
		
		Method methodResult = null;
		
		for(Method method : methods) {
			
			if(method.getName() == OperationName) {
				methodResult = method;
			}
			
		}
		return methodResult;
	}
	
	@Override
	public void run () {

		while ( true) {
		
			try {
				
				VectorTasks tasks = buffer.pop();
				
				double value;
				
				if(tasks.getSeqVector() == null) {

					seqVector = new SeqVector(tasks.getPositions().size());
					
					loadValues(seqVector, tasks.getValues());
					
					Method method = getMethod(tasks.getOperationName());
					
					value = (double) method.invoke(seqVector);
					
					System.out.println("Valor que resolvio el invoke: " + value + " Por el THREAD:" + this.id);

					VectorResult vectorResult = new VectorResult(value, tasks.getOperationName());
				
					vectorManagerResult.getResults().add(vectorResult);
				
				}else {
					
					seqVector = tasks.getSeqVector();
					
					System.out.println("WORKER " + tasks.getOperationName() + " ID: " + this.id);
					
					Method method = getMethod(tasks.getOperationName());
					
					method.invoke(seqVector, tasks.getPositions().get(0), tasks.getValues().get(0));
					
					System.out.println("POSITION: " + tasks.getPositions().get(0) + " VALUE: " + tasks.getValues().get(0));
				
				}
				
				
			
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	
	}

}
