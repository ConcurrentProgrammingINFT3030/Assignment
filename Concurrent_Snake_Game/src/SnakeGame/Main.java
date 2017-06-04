package SnakeGame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application entry point
 * @author CQ, RT
 */
public class Main {
	public static int serverMax = 104;
	public static void main(String[] args) {
		
		
		ExecutorService executor = Executors.newFixedThreadPool(serverMax);
		BoundedBuffer buffer = new BoundedBuffer(serverMax);
		Server s = new Server(buffer, executor);
		
		s.addUsers();

		executor.execute(s);

		for (int i = 0; i < serverMax; i++) {
			Client c = new Client(buffer, i);
			if (s.connect(c)) {
				executor.execute(c);
			}
		}

		executor.shutdown();
		  
		System.out.println("Finished all threads");
	}
}
