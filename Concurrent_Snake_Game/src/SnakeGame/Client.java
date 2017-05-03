package SnakeGame;

/**
 * A client that connects to a server
 * @author CQ
 */
public class Client {
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
