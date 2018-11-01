package ar.unq.concu.model;

import java.util.ArrayList;
import java.util.Arrays;
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
	

	/** Pone el valor d en todas las posiciones del vector. 
	 * @param d, el valor a ser asignado. */
	public void set(double d) {
		
		int count = 0;
		
		while(dimension() > count) {

			for (int i = 0; i < dimension(); ++i) {
				generateTasks(i, d, this, "setWithPosition");
				count++;
			}
			
			while(!getBuffer().getVectorTasksIsEmpty());
		}	
			
	}

	/** Copia los valores de otro vector sobre este vector.
	 * @param v, el vector del que se tomaran los valores nuevos.
	 * @precondition dimension() == v.dimension(). */
	public void assign(SeqVector v) {
		for (int i = 0; i < dimension(); ++i)
			setWithPosition(i, v.get(i));
	}
	
	
	/** Copia algunos valores de otro vector sobre este vector.
	 * Un vector mascara indica cuales valores deben copiarse.
	 * @param mask, vector que determina si una posicion se debe copiar.
	 * @param v, el vector del que se tomaran los valores nuevos.
	 * @precondition dimension() == mask.dimension() && dimension() == v.dimension(). */
	public void assign(SeqVector mask, SeqVector v) {
		for (int i = 0; i < dimension(); ++i)
			if (mask.get(i) >= 0)
				setWithPosition(i, v.get(i));
	}
	
	
    /** Suma los valores de este vector con los de otro (uno a uno).
	 * @param v, el vector con los valores a sumar.
	 * @precondition dimension() == v.dimension(). */
	public void add(SeqVector v) {
		for (int i = 0; i < dimension(); ++i)
			setWithPosition(i, get(i) + v.get(i));
	}
	
	
	/** Multiplica los valores de este vector con los de otro
     *  (uno a uno).
	 * @param v, el vector con los valores a multiplicar.
	 * @precondition dimension() == v.dimension(). */
	public void mul(SeqVector v) {

		while(vectorManagerResult.getResults().isEmpty() || vectorManagerResult.getResults().size() > 1) {
			
			
			
		}
		for (int i = 0; i < dimension(); ++i)
			setWithPosition(i, get(i) * v.get(i));
	}
	
	
	/** Obtiene el valor absoluto de cada elemento del vector. */
	public void abs() {
		for (int i = 0; i < dimension(); ++i)
			setWithPosition(i, Math.abs(get(i)));
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
	
	
    /** Obtiene el valor maximo en el vector. 
     * @throws  */
	public synchronized double max() {
		
		System.out.println( "EXECUTE METHOD MAX" );
		
		System.out.println( "GENERATE TASKS" );
		List<VectorTasks> taks = new ArrayList<>();
		
		initMap();
		
		while (vectorManagerResult.getResults().isEmpty() || vectorManagerResult.getResults().size() > 1) {
			
			System.out.println( "EXECUTE WHILE" );

			if(vectorManagerResult.getResults().isEmpty()) {
				
				System.out.println( "GENERATE TASKS BY DIMENSION" );
				taks = generateTasks(dimension()-1, getTotalThread(), "max");
			
				System.out.println( "SAVE TASKS IN BUFFER" );
				saveTasks(taks);
			}
			else {
				System.out.println( "GENERATE TASKS BY RESULT" );
				taks = generateTasksByResult(vectorManagerResult.getResults().size(), getTotalThread(), "max");
				saveTasks(taks);
			}
			
			while(vectorManagerResult.getResults().isEmpty());
			
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		}	
		
		System.out.println("Result:");
		
		//chequeo que el managerResult no este vacio
		
		Integer lastElement = getVectorManagerResult().getResults().size() - 1;
		
		return getVectorManagerResult().getResults().get(lastElement).getValue();

	}

	private void saveTasks(List<VectorTasks> tasks) {
		System.out.println( "SAVE TASKS IN BUFFER" );
		for(VectorTasks vectorTaks : tasks) {
			try {
				getBuffer().push(vectorTaks);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<VectorTasks> generateTasksByResult(int dimension, int totalThread, String nameOperation){
		
		
		List<VectorTasks> taks = new ArrayList<>();
		System.out.println( "generateTasksByResult dimension: " + dimension+ " totalThreads: " + totalThread);

		taks = splitVectorForThreadsByResult(taks, dimension, totalThread,nameOperation);
		
		return taks;

	}

	private List<VectorTasks> generateTasks(int dimension, int totalThread, String nameOperation) {
		
		List<VectorTasks> taks = new ArrayList<>();
		
		mapResult = splitVectorForThreads(mapResult, dimension, totalThread);
		
		mapResult.forEach((key,values)->{
			
			List<Integer> positions = values.stream().map(position -> position.getPosition()).collect(Collectors.toList());
			List<Double> valuesForVector = values.stream().map(position -> position.getValue()).collect(Collectors.toList());
			VectorTasks vectorTaks = new VectorTasks(nameOperation, valuesForVector, positions);
			taks.add(vectorTaks);
		
		});
		
		return taks;
	}
	
	List<VectorTasks> splitVectorForThreadsByResult ( List<VectorTasks> taks , int dimension, int totalThread,String nameOperation){
		System.out.println( "splitVectorForThreadsByResult dimension: " + dimension );
		if(dimension == 3 ) {
		
			taks.add(createTasks(Arrays.asList(0, 1, 2), nameOperation));
		
		}else {
						
			taks.add(createTasks(Arrays.asList(0, 1), nameOperation));

			if(dimension >= 2) {
				int dimensionLocal = dimension-2 ;
				taks.addAll(splitVectorForThreadsByResult(taks, dimensionLocal, totalThread, nameOperation));
			}

		}
		
		return taks;
	}
	
	private VectorTasks createTasks(List<Integer> positions, String nameOperation) {
		
		List<Double> values  = new ArrayList<>();
		List<Integer> positionForVector  = new ArrayList<>();
		
		for (int i = 0; i < positions.size(); i++) {

			System.out.println( "index: " + i + " position: " + positions.get(i));
			values.add(vectorManagerResult.getResults().get(positions.get(i)).getValue());
			vectorManagerResult.getResults().remove(positions.get(i));
			positionForVector.add(positions.get(i));
		
		}
		
		VectorTasks vectorTaks = new VectorTasks(nameOperation, values, positionForVector);

		return vectorTaks;
	}
	
	HashMap<Integer, List<Position>> splitVectorForThreads (HashMap<Integer, List<Position>> mapResult, int dimension, int totalThread){
		
		int dimensionLocal = dimension;
		int threadLocal = totalThread;
		
		if(dimensionLocal <= threadLocal) {
			

			for (int i = 0; i < dimensionLocal; i++) {
				System.out.println("dimension menor a dimension: "+i+" totalThread: "+totalThread);
				List<Position> positions =  mapResult.get(i);
				positions.add(new Position(getElements()[i], i));
				mapResult.put(i, positions);
			}
		
		}else {
			
			if(dimensionLocal >= threadLocal) {
				for (int i = 0; i < threadLocal; i++) {
					List<Position> positions =  mapResult.get(i);
					System.out.println("dimension:"+dimensionLocal);
					positions.add(new Position(getElements()[dimensionLocal], dimensionLocal));
					mapResult.put(i, positions);
					dimensionLocal = dimensionLocal - 1;
				}
			}

			mapResult = splitVectorForThreads(mapResult, dimensionLocal, threadLocal);	
		
		}
			
		return mapResult;
	}
	
	private void generateTasks(int index, double value, SeqVector seqVector, String nameOperation){
		
		VectorTasks vectorTaks = new VectorTasks(nameOperation, Arrays.asList(value), Arrays.asList(index), seqVector);
		
		try {
			getBuffer().push(vectorTaks);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	
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
