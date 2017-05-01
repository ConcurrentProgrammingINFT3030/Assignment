package SnakeGame;

public class Main {
	public static void main(String[] args) {
		Server s = new Server();
		Client c = new Client("player 2");
		
		s.connect(c);
		
		s.run();
	}
}
