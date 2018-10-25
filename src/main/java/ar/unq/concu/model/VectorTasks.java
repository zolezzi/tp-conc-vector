package ar.unq.concu.model;

import java.util.ArrayList;
import java.util.List;

public class VectorTasks {
	
	private String operationName;	
	private List<Double> values = new ArrayList<>();
	private List<Integer> positions = new ArrayList<>();
	
	public VectorTasks(String nameOperation, List<Double> values, List<Integer> positions) {
		super();
		this.operationName = nameOperation;
		this.values = values;
		this.positions = positions;
	}

	public String getOperationName() {
		return operationName;
	}
	
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public List<Double> getValues() {
		return values;
	}

	public List<Integer> getPositions() {
		return positions;
	}

	public void setPositions(List<Integer> positions) {
		this.positions = positions;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}
	
}
