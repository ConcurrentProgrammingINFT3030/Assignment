package SnakeGame;
/**
 * 
 * @author RT, David Kearney lecture slides
 */
public class BoundedBuffer {

	private int size;
	private int[] B;
	private int InPtr = 0, OutPtr = 0;
	private int Count = 0;

	public  BoundedBuffer(int theSize) {
		size = theSize;

		B = new int[size];
	}

	public synchronized void append(int value) {
		System.out.println("Attempting Append");
		while (Count == size) 
		{
			System.out.println("Waiting for append");
			try 
			{
				this.wait();
			} 
			catch (InterruptedException e) 
			{
				System.out.println("Append interrupted");
			}
			
		}
		//System.out.println("Completing Append");
		B[InPtr] = value;
		System.out.println("                      "+Thread.currentThread().getName()+" added "+value+" at "+InPtr+" Count was= " +Count);

		InPtr = (InPtr + 1) % size;
		Count = Count + 1;
		//System.out.println("Append Finished");
		this.notifyAll();
	}
	public synchronized int take() {
		System.out.println("Attempting take");
		while (Count==0) 
		{
			System.out.println("Waiting for take");
			try 
			{ 
				wait();
			} catch (InterruptedException e) {}
		}
		System.out.println("Completing take");
		int direction = B[OutPtr];
		System.out.println("                      "+Thread.currentThread().getName()+" removed "+direction+" at "+OutPtr+" Count was= "+Count);
		OutPtr = (OutPtr+1) % size;
		Count = Count-1;
		notifyAll();
		return direction;
	}

}