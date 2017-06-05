package SnakeGame;

import org.mapdb.*;

import java.io.File;
import java.util.concurrent.*;
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

	private ExecutorService executor;
	private BoundedBuffer buffer;

	public Server(BoundedBuffer b, ExecutorService executor) {
		this.executor = executor;
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
		for (int i = 0; i < 104; i++) {
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
	public boolean connect(Client client) {
		//This thread uses an anonymous Callable<boolean> to authenticate the client
		//We use a future to retrieve the result once the thread finishes
		Future<Boolean> future = executor.submit((Callable<Boolean>)() -> authenticate(client));
		try {
			return future.get();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Authenticates the client against the db before connecting them to the game
	 * @param client the client to authenticate
	 */
	private boolean authenticate(Client client) {
		//Authenticate against DB.
		//The clients username is their ID (0-103)
		//and their password is "Client:{ID}"
		if (map.get(client.Id).equals(client.toString())) {
			game.createSnake(client);
			return true;
		}

		System.out.println("UNREGISTERED PLAYER ID: " + client.Id);
		return false;
	}

	/**
	 * Runs the server. This method is blocking
	 */
	public void run() {
		//This is the main loop of the server.
		//It blocks the main thread and processes the game until it is exited
		while (!game.paused && !game.game_over)
		{
			try
			{
				//System.out.println("Running");
				game.cycleTime = System.currentTimeMillis();

				//add player moves to the buffer
				if (game.playerList.size() > 3)
				{
					if (game.getSnake(0).getClient().getActive())
					{
						buffer.append(new MoveData(game.P1_next_direction, 0));
					}
					if (game.getSnake(1).getClient().getActive())
					{
						buffer.append(new MoveData(game.P2_next_direction, 1));
					}
					if (game.getSnake(2).getClient().getActive())
					{
						buffer.append(new MoveData(game.P3_next_direction, 2));
					}
					if (game.getSnake(3).getClient().getActive())
					{
						buffer.append(new MoveData(game.P4_next_direction, 3));
					}
				}

				for(Snake snake: game.playerList) {
					if (!snake.getClient().getActive()) {
						continue;
					}

					//take the next player move from the buffer
					MoveData data = buffer.take();

					if (data.getId() == 0) {
						game.P1_direction = data.getDirection();
						game.moveSnake(game.getSnake(0), data.getDirection(), -1);
					} else if (data.getId() == 1) {
						game.P2_direction = data.getDirection();
						game.moveSnake(game.getSnake(1), data.getDirection(), -1);
					} else if (data.getId() == 2) {
						game.P3_direction = data.getDirection();
						game.moveSnake(game.getSnake(2), data.getDirection(), -1);
					} else if (data.getId() == 3) {
						game.P4_direction = data.getDirection();
						game.moveSnake(game.getSnake(3), data.getDirection(), -1);
					} else if (data.getId() > 3) {
						game.moveSnake(game.getSnake(data.getId()), data.getDirection(), -1);
					}
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
