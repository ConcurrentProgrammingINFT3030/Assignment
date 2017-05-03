package SnakeGame;

/**
 * A client that connects to a server
 * @author CQ
 */
public class Client implements Runnable{
	
	@Override
	public void run() {
		try {
				System.out.println("Test running");
	         }
	      catch (Exception e) {
	         System.out.println("Thread interrupted.");
	      }
	      System.out.println("Thread exiting.");
	}
	/**
	 * Client's ID
	 */
	public int Id;
	
	public Client(int id) {
		this.Id = id;
	}

	@Override
	public String toString() {
		return "Client:" + Id;
	}

	
}
