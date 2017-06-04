package SnakeGame;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;



/**
 * A client that connects to a server
 * @author CQ, RT
 */
public class Client implements Runnable{

	/**
	 * Client's ID
	 */
	public int Id;
	private boolean active;

	private int lastDir = -1;
	/**
	 * Client's buffer
	 */
	private BoundedBuffer buffer;
	public Client(BoundedBuffer b, int id) 
	{
        buffer = b;
        this.Id = id;
        active = true;
    }

    public synchronized void setActive(boolean active) {
		this.active = active;
	}

	public synchronized boolean getActive() {
		return active;
	}

	@Override
	public void run() {
		
		while (active)
		{
			try {
				//Only AIs need to produce random input
				if (Id > 4)
				{
					Random randomDirection = new Random();
					int random = randomDirection.nextInt(4);
					boolean even = random % 2 == 0;
					//An even direction + 1 = the opposite direction.
					//An odd direction - 1 = the opposite direction.
					//AI snakes shouldn't be able to do a 180, so this should ensure they
					//can only turn at most 90degrees
					while ((even && random + 1 == lastDir) || (!even && random - 1 == lastDir)) {
						random = randomDirection.nextInt(4);
						even = random % 2 == 0;
					}

					lastDir = random;

					buffer.append(random);
					Thread.sleep(100);
				}
			}
			catch (Exception e) {
		         System.out.println("Client Thread Interrupted.");
			}
		}
		System.out.println("Thread exiting.");
	}


	@Override
	public String toString() {
		return "Client:" + Id;
	}
}
