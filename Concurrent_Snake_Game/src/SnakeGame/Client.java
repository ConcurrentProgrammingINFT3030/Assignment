package SnakeGame;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;


/**
 * A client that connects to a server
 * @author CQ, RT
 */
public class Client implements Runnable{
	

	 
	/**
	 * Client's ID
	 */
	public int Id;
	public boolean active = true;
	/**
	 * Client's buffer
	 */
	private BoundedBuffer buffer;
	
	public Client(BoundedBuffer b, int id) 
	{
        buffer = b;
        this.Id = id;
    }

	@Override
	public void run() {
		while (active == true)
		{
			try {
				
				//System.out.println("Client" + Thread.currentThread().getName() + " running");
				//for the players
				if (Id < 5)
				{
					//player input
				}
				
				//for the non-players
				else if (Id > 4)
				{
					System.out.println("Attempting random direction append");
					Random randomDirection = new Random();
					int value = randomDirection.nextInt(4);
					//buffer.append(value);
				}
				
			}
		      catch (Exception e) {
		         System.out.println("Client Thread Interruped.");
		      }
		      
		}
		System.out.println("Thread exiting.");
		
	}


	@Override
	public String toString() {
		return "Client:" + Id;
	}

	
}
