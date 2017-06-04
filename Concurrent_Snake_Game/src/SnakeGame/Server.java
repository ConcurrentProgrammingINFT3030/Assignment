package SnakeGame;

import org.mapdb.*;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles communication with players and updates the game screen
 * @author CQ, RT
 */
public class Server implements Runnable{
	private DB db;
	private ConcurrentNavigableMap<Integer, String> map;
	public Game game;

	private BoundedBuffer buffer;

	public Server(BoundedBuffer b) {
		buffer = b;
		db = DBMaker.newFileDB(new File("snakes"))
				.closeOnJvmShutdown()
				.make();
		map = db.getTreeMap("authentication");

		game = new Game();
		game.init();
	}

	/**
	 * Adds 104 users to the database, if they don't already exist
	 */
	public void addUsers(){
		for (int i = 1; i < 105; i++) {
			if (!map.containsKey(i)) {
				map.put(i, "Client:"+i);
			}
		}

		db.commit();
	}

	/**
	 * Connects a client to the server
	 * @param client the client to connect to the server
	 */
	public void connect(Client client) {
		//This thread uses an anonymous runnable to authenticate the client
		//() -> authenticate(client) is equivalent to:
		/*
			new Runnable() {
				@Override
				public void run() {
					authenticate(client);
				}
			}
		*/
		Thread t = new Thread(() -> authenticate(client));
		t.start();
	}

	/**
	 * Authenticates the client against the db before connecting them to the game
	 * @param client the client to authenticate
	 */
	private void authenticate(Client client) {
		//Authenticate against DB.
		//The clients username is their ID (1-104)
		//and their password is "Client:{ID}"
		if (map.get(client.Id).equals(client.toString())) {
			game.createSnake(client);
		}
		else {
			System.out.println("UNREGISTERED PLAYER ID: " + client.Id);
		}
	}

	/**
	 * Runs the server. This method is blocking
	 */
	public void run() {
		//This is the main loop of the server.
		//It blocks the main thread and processes the game until it is exited
		while (game.paused == false && game.game_over == false)
		{
			try
			{
				//System.out.println("Running");
				game.cycleTime = System.currentTimeMillis();
				int count = 0;
				for(Snake i: game.playerList){
					if(count == 0 && i != null){
						//Directions for arrow keys
						game.P1_direction = game.P1_next_direction;
						game.moveSnake(i,game.P1_direction,game.P1_next_direction);
					} else if(count == 1 && i != null){
						//Directions for WASD
						game.P2_direction = game.P2_next_direction;
						game.moveSnake(i,game.P2_direction,game.P2_next_direction);
					} else if(count == 2 && i != null){
						//Directions for NUMPAD
						game.P3_direction = game.P3_next_direction;
						game.moveSnake(i,game.P3_direction,game.P3_next_direction);
					} else if(count == 3 && i != null){
						//Directions for IJKL
						game.P4_direction = game.P4_next_direction;
						game.moveSnake(i,game.P4_direction,game.P4_next_direction);
					} else if (count > 3 && i != null) {
						game.randomDirection(count);
						game.directionList.set(count, buffer.take());
						
						game.moveSnake(game.playerList.get(count), game.directionList.get(count), game.next_directionList.get(count));
					}
					count+=1;
					
				}
				game.renderGame();
				game.cycleTime = System.currentTimeMillis() - game.cycleTime;
				game.sleepTime = game.speed - game.cycleTime;
				if (game.sleepTime < 0)
					game.sleepTime = 0;
				try {
					Thread.sleep(game.sleepTime);
				} catch (InterruptedException ex) {
					Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null,
							ex);
				}
				
				
			}	
			catch (RuntimeException e) {
				System.out.println("Thread interrupted.");
			}
		}
		
	}
}
