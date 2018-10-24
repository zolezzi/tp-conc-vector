package ar.unq.concu.model;

import java.util.ArrayList;
import java.util.List;

public class VectorTaks {
	
	String NameOperation;	
	List<Double> values = new ArrayList<>();
	List<Integer> positions = new ArrayList<>();
	
	public VectorTaks(String nameOperation, List<Double> values, List<Integer> positions) {
		super();
		NameOperation = nameOperation;
		this.values = values;
		this.positions = positions;
	}

	public String getNameOperation() {
		return NameOperation;
	}
	
	public void setNameOperation(String nameOperation) {
		NameOperation = nameOperation;
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
