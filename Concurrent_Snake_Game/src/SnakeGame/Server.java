package SnakeGame;

public class Server {
	private Game game;
	private boolean running;
	
	public Server() {
		game = new Game();
		game.init();
	}
	
	/**
	 * Connects a client to the server
	 * @param client
	 */
	public void connect(Client client) {
		//login with client.name
		//This will spawn us a new thread which we can use
		
		game.createSnake();
	}
	
	/**
	 * Runs the server. This method is blocking
	 */
	public void run() {
		//This should be a blocking call so the main thread does not exit
		//game.mainLoop is already blocking, but we should change this for concurrent-ness
		game.mainLoop();
		
		//while (running) {
		//}
	}
}
