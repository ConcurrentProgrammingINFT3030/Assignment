package SnakeGame;

import org.mapdb.*;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Handles communication with players
 * @author CQ
 */
public class Server {
    private DB db;
    private ConcurrentNavigableMap<Integer, String> map;
	private Game game;
	private boolean running;
	
	public Server() {
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
		Thread t = new Thread(() -> authenticate(client));
        t.run();
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
            //Concurrency is required here. The player thread (Thread.currentThread())
            //could be passed to createSnake
            game.createSnake();
        }
        else {
            System.out.println("UNREGISTERED PLAYER ID: " + client.Id);
        }
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
