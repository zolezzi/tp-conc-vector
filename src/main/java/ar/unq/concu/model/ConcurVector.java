package ar.unq.concu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ConcurVector extends SeqVector{
		
	private int totalThread;
	private Buffer buffer;
	private VectorManagerResult vectorManagerResult;
	private HashMap<Integer, List<Position>> mapResult = new HashMap<>();
	
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
		
		System.out.println( "EXECUTE METHOD MAX" );
		
		while (vectorManagerResult.getResults().isEmpty() || vectorManagerResult.getResults().size() > 1) {
			
			System.out.println( "GENERATE TASKS" );
			List<VectorTasks> taks = new ArrayList<>();
			
			initMap();
			//Fijarme como particionar el vector apatrir de dimension y la cantidad de thread
			//Refactorizar en un method si funciona.
			if(vectorManagerResult.getResults().isEmpty()) {
				taks = generateTasks(dimension(), getTotalThread(), "max");
			}
			else {
				taks = generateTasks(vectorManagerResult.getResults().size()-1, getTotalThread(), "max");
			}		
			
			//Genero la n tareas que necesito
			for(VectorTasks vectorTaks : taks) {
				try {
					getBuffer().push(vectorTaks);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}	
		
		System.out.println("Result:");
		
		//chequeo que el managerResult no este vacio
		return getVectorManagerResult().getResults().stream().findFirst().get().getValue();

	}


	private List<VectorTasks> generateTasks(int dimension, int totalThread, String nameOperation) {
		
		List<VectorTasks> taks = new ArrayList<>();
		
		mapResult = splitVectorForThreads(mapResult, dimension-1, totalThread);
		
		mapResult.forEach((key,values)->{
			
			List<Integer> positions = values.stream().map(position -> position.getPosition()).collect(Collectors.toList());
			List<Double> valuesForVector = values.stream().map(position -> position.getValue()).collect(Collectors.toList());
			VectorTasks vectorTaks = new VectorTasks(nameOperation, valuesForVector, positions);
			taks.add(vectorTaks);
		});
		
		return taks;
	}
	
	
	
	HashMap<Integer, List<Position>> splitVectorForThreads (HashMap<Integer, List<Position>> mapResult, int dimension, int totalThread){
		
		if(dimension < totalThread) {
			
			for (int i = 0; i < dimension; i++) {
				List<Position> positions =  mapResult.get(i);
				positions.add(new Position(getElements()[i], i));
				mapResult.put(i, positions);
			}
		}else {
			
			for (int i = 0; i < totalThread; i++) {
				List<Position> positions =  mapResult.get(i);
				positions.add(new Position(getElements()[dimension], dimension));
				mapResult.put(i, positions);
				dimension = dimension - 1;
			}
			
			splitVectorForThreads(mapResult, dimension, totalThread);
			
		}
		
		return mapResult;
	}
	
	private void initMap () {
		for (int i = 0; i < totalThread; i++) {
			mapResult.put(i, new ArrayList<>());
		}
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


	public HashMap<Integer, List<Position>> getMapResult() {
		return mapResult;
	}


	public void setMapResult(HashMap<Integer, List<Position>> mapResult) {
		this.mapResult = mapResult;
	}
	
}
