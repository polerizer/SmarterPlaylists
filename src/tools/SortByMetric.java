/**
 * Nov 10, 2013
 * SortByMetric.java
 * Daniel Pok
 * AP Java 6th
 */
package tools;


/**
 * @author Daniel
 *
 */
public class SortByMetric<T> implements Comparable<SortByMetric<T>>{
	
	private T value;
	private double key;
	
	public SortByMetric(T mValue, double mKey){
		value = mValue;
		key = mKey;
	}

	public double key(){
		return key;
	}
	
	public T value(){
		return value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SortByMetric<T> o) {
		// TODO Auto-generated method stub
		double diff = key - o.key();
		if(diff > 0){
			return 1;
		} else if(diff == 0){
			return 0;
		} else{
			return -1;
		}
	}
}
