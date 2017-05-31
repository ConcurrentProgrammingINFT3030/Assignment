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
		while (Count == size) 
		{
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
			B[InPtr] = value;
			System.out.println("                      "+Thread.currentThread().getName()+" added "+value+" at "+InPtr+" Count was= " +Count);

			InPtr = (InPtr + 1) % size;
			Count = Count + 1;
			
			this.notifyAll();
		}
	}
	public synchronized int take () {
	    while (Count==0) 
	    {
	        try 
	        { 
	        	wait();
	        } catch (InterruptedException e) {}
	    }
	    int direction = B[OutPtr];
	    System.out.println("                      "+Thread.currentThread().getName()+" removed "+direction+" at "+OutPtr+" Count was= "+Count);
	    OutPtr = (OutPtr+1) % size;
	    Count = Count-1;
	    notifyAll();
	    return direction;
	}

}