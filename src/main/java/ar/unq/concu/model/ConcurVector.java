package ar.unq.concu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConcurVector extends SeqVector{
		
	private int totalThread;
	private Buffer buffer;
	private VectorManagerResult vectorManagerResult;
	private HashMap<Integer, List<Double>> mapResult = new HashMap<>();
	
	
	public ConcurVector(int dimension, int totalThread, Buffer buffer, VectorManagerResult vectorManagerResult) {
		super(dimension);
		this.totalThread = totalThread;
		this.buffer = buffer;
		this.vectorManagerResult = vectorManagerResult;
	}


	/** Copia los valores de otro vector sobre este vector.
	 * @param v, el vector del que se tomaran los valores nuevos.
	 * @precondition dimension() == v.dimension(). */
	public void assign(SeqVector v) {
		for (int i = 0; i < dimension(); ++i)
			set(i, v.get(i));
	}
	
	
	/** Copia algunos valores de otro vector sobre este vector.
	 * Un vector mascara indica cuales valores deben copiarse.
	 * @param mask, vector que determina si una posicion se debe copiar.
	 * @param v, el vector del que se tomaran los valores nuevos.
	 * @precondition dimension() == mask.dimension() && dimension() == v.dimension(). */
	public void assign(SeqVector mask, SeqVector v) {
		for (int i = 0; i < dimension(); ++i)
			if (mask.get(i) >= 0)
				set(i, v.get(i));
	}
	
	
    /** Suma los valores de este vector con los de otro (uno a uno).
	 * @param v, el vector con los valores a sumar.
	 * @precondition dimension() == v.dimension(). */
	public void add(SeqVector v) {
		for (int i = 0; i < dimension(); ++i)
			set(i, get(i) + v.get(i));
	}
	
	
	/** Multiplica los valores de este vector con los de otro
     *  (uno a uno).
	 * @param v, el vector con los valores a multiplicar.
	 * @precondition dimension() == v.dimension(). */
	public void mul(SeqVector v) {
		for (int i = 0; i < dimension(); ++i)
			set(i, get(i) * v.get(i));
	}
	
	
	/** Obtiene el valor absoluto de cada elemento del vector. */
	public void abs() {
		for (int i = 0; i < dimension(); ++i)
			set(i, Math.abs(get(i)));
	}


	/** Obtiene la suma de todos los valores del vector. */
	public double sum() {
		double result = 0;
		for (int i = 0; i < dimension(); ++i)
			result += get(i);
		return result;
	}
    
    
    /** Obtiene el valor promedio en el vector. */
	public double mean() {
        	double total = sum();
        	return total / dimension();
	}
    
    
	/** Retorna el producto de este vector con otro.
     * El producto vectorial consiste en la suma de los productos
     * de cada coordenada.
	 * @param v, el vector a usar para realizar el producto.
	 * @precondition dimension() == v.dimension(). */
	public double prod(SeqVector v) {
		SeqVector aux = new SeqVector(dimension());
		aux.assign(this);
		aux.mul(v);
		return aux.sum();
	}
	
	
	/** Retorna la norma del vector.
     *  Recordar que la norma se calcula haciendo la raiz cuadrada de la
     *  suma de los cuadrados de sus coordenadas.
     */
	public double norm() {
		SeqVector aux = new SeqVector(dimension());
		aux.assign(this);
		aux.mul(this);
		return Math.sqrt(aux.sum());
	}
	
	
    /** Obtiene el valor maximo en el vector. */
	public double max() {
		
		while (vectorManagerResult.getResults().isEmpty() || vectorManagerResult.getResults().size() > 1) {
			
			List<VectorTaks> taks = new ArrayList<>();
			
			//Fijarme como particionar el vector apatrir de dimension y la cantidad de thread
			//Refactorizar en un method si funciona.
			if(vectorManagerResult.getResults().isEmpty()) {
				taks = generateTaks(dimension(), getTotalThread());
			}
			else {
				taks = generateTaks(vectorManagerResult.getResults().size(), getTotalThread());
			}		
			
			//Genero la n tareas que necesito
			for(VectorTaks vectorTaks : taks) {
				try {
					getBuffer().push(vectorTaks);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}	
		
		//chequeo que el managerResult no este vacio
		return getVectorManagerResult().getResults().stream().findFirst().get().getValue();

	}


	private List<VectorTaks> generateTaks(int dimension, int totalThread2) {
		
		double  = dimension() / getTotalThread();
		double 
		
		if() {}
		
		return null;
	}


	public int getTotalThread() {
		return totalThread;
	}


	public void setTotalThread(int totalThread) {
		this.totalThread = totalThread;
	}


	public Buffer getBuffer() {
		return buffer;
	}


	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}


	public VectorManagerResult getVectorManagerResult() {
		return vectorManagerResult;
	}


	public void setVectorManagerResult(VectorManagerResult vectorManagerResult) {
		this.vectorManagerResult = vectorManagerResult;
	}

	
}
