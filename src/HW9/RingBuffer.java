package HW9;

public class RingBuffer<E>
{
	private transient Object[] arr;
	private int size = 0, firstIndex = 0, lastIndex = 0;

	
	public RingBuffer(int capacity)
	{
		arr = new Object[capacity];
	}

	
	
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	

	public int size()
	{
		return size;
	}

	
	
	public int capacity()
	{
		return arr.length;
	}

	
	
	/**Adds an element to this RingBuffer, will overwrite the oldest data.*/
	public E enqueue(E item)
	{
		E old = (E)arr[lastIndex];

		arr[lastIndex] = item;
		if (lastIndex == firstIndex)
			firstIndex = (firstIndex + 1) % arr.length;
		
		lastIndex = (lastIndex + 1) % arr.length;	// wrap-around
		if (size < arr.length)
			size++;

		return old;
	}

	
	
	/**Removes the oldest item from this RingBuffer.*/
	public E dequeue()
	{
		if (isEmpty())
			throw new IndexOutOfBoundsException("Ring buffer empty");
		
		E item = (E)arr[firstIndex];
		arr[firstIndex] = false;
		size--;
		firstIndex = (firstIndex + 1) % arr.length;
		return item;
	}

	
	
	/**Returns the Object at the position index without removing it from the array.**/
	public E peek(int index)
	{
		if (isEmpty())
			throw new IndexOutOfBoundsException("Ring buffer empty");
		return (E)arr[(firstIndex + index) % size];
	}
	
	

	/*public void set(E item, int index)
	{
		set(item, index, 0);
	}
	
	

	public void set(E item, int index, int originIndex)
	{
		arr[(index + originIndex) % size] = item;
	}*/

	
	
	@Override
	public String toString()
	{
		String str = size + " {";
		for (int j = 0; j < size() - 1; j++)
			str += peek(j).toString() + ", ";
		str += peek(size()-1).toString();
		return str + "}";			
	}
}
