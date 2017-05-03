package SnakeGame;

/**
 * Application entry point
 * @author CQ
 */
public class Main {
	public static void main(String[] args) {
		Server s = new Server();
		s.addUsers();

		for (int i = 1; i < 5; i++) {
			//For the moment we're only connecting 2 players
			Client c = new Client(i);
			s.connect(c);
		}

		s.run();
	}
}
