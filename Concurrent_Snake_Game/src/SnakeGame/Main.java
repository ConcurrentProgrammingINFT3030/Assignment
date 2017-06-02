package SnakeGame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application entry point
 * @author CQ, RT
 */
public class Main {
	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newFixedThreadPool(105);
		BoundedBuffer buffer = new BoundedBuffer(100);
		Server s = new Server(buffer);
		
		s.addUsers();

		for (int i = 1; i < 101; i++) {
			Client c = new Client(buffer, i);
			s.connect(c);
			
			Runnable client = new Client(buffer, i);
			executor.execute(client);
			
			//replaced by threadpool
			//Thread c1 = new Thread(c);
			//c1.start();
			
		}

		Thread server = new Thread(s);
		server.start();
		 while (!executor.isTerminated()) {
			 
		 }  
		  
	        System.out.println("Finished all threads"); 
	}
}
