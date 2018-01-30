import java.util.*;

public class FixedQueue<E> extends ArrayList<E>	{
	private int maxSize;

	public FixedQueue(int size){
		this.maxSize = size;
	}

	public boolean add (E e){
		boolean r = super.add(e);
		
		if(size() > maxSize){
			removeRange(0, size() - maxSize - 1);
		}

		return r;
	}

	public E getYoungest(){
		return get(size() - 1);
	}

	public E getOldest(){
		return get(0);
	}
}
