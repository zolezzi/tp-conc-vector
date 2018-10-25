package ar.unq.concu.model;

import java.lang.reflect.Method;
import java.util.List;

public class Worker extends Thread{
	
	private Buffer buffer;
	private VectorManagerResult vectorManagerResult;
	private SeqVector seqVector;
	private Method[] methods;
	
	public Worker(Buffer buffer, VectorManagerResult vectorManagerResult) {
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
			seqVector.set(values.get(i));
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

		while ( !buffer.getVectorTasksIsEmpty()) {
		
			try {
				
				VectorTasks tasks = buffer.pop();
				
				seqVector = new SeqVector(tasks.getPositions().size());
				
				loadValues(seqVector, tasks.getValues());
				
				Method method = getMethod(tasks.getOperationName());
				
				double value = (double) method.invoke(seqVector);

				VectorResult vectorResult = new VectorResult(value, tasks.getOperationName());
				
				vectorManagerResult.getResults().add(vectorResult);
			
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	
	}

}
