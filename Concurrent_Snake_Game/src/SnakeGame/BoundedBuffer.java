package SnakeGame;
/**
 * 
 * @author RT, David Kearney lecture slides
 */
public class BoundedBuffer {

	private int size;
	private int[] B;
	private MoveData[] C;
	private int InPtr = 0, OutPtr = 0;
	private int Count = 0;

	public  BoundedBuffer(int theSize) {
		size = theSize;

		//B = new int[size];
		C = new MoveData[size];
	}

	/**
	 * Appends a value to the end of the buffer in a thread-safe manner
	 * @param value
	 */
	public boolean ready()
	{
		if (Count + 5 < size)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public synchronized void append(MoveData value) {
		while (Count == size) 
		{
			try 
			{
				System.out.println("Waiting for append");
				this.wait();
			} 
			catch (InterruptedException e) 
			{
			}
		}
		//System.out.println("Appending" + value.toString());
		//B[InPtr] = value;
		C[InPtr] = value;
		//System.out.println("                      "+Thread.currentThread().getName()+" added "+value+" at "+InPtr+" Count was= " +Count);

		InPtr = (InPtr + 1) % size;
		Count = Count + 1;
		this.notifyAll();
	}

	/**
	 * Takes a value from the start of the buffer in a thread-safe manner
	 * @return
	 */
	public synchronized MoveData take() {
		while (Count==0) 
		{
			try 
			{
				System.out.println("Waiting for take");
				this.wait();
			} catch (InterruptedException e) {}
		}
		//System.out.println("Taking + " + Count + "/" + size);
		//int direction = B[OutPtr];
		MoveData direction = C[OutPtr];
		//System.out.println(direction.toString());
		//System.out.println("                      "+Thread.currentThread().getName()+" removed "+direction+" at "+OutPtr+" Count was= "+Count);
		OutPtr = (OutPtr+1) % size;
		Count = Count-1;
		this.notifyAll();
		return direction;
	}

}