package ar.unq.concu.model;

public class VectorResult {
	
	private double value;
	private String operationName;

	public VectorResult(double value, String operationName) {
		super();
		this.value = value;
		this.operationName = operationName;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
}
