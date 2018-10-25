package ar.unq.concu.model;

public class Position {
	
	double value;
	int position;
	
	public Position() {
		
	}

	public Position(double value, int position) {
		super();
		this.value = value;
		this.position = position;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
