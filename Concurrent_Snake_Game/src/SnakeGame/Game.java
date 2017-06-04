package SnakeGame;

/**
 * Original Created by https://code.google.com/p/java-snake/source/browse/trunk/java-snake/src/snake/Main.java
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import SnakeGame.Entity.Type;

/**
 *
 * @author Group
 */
public class Game implements KeyListener, WindowListener {
	
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	public int P1_direction = -1;
	public int P1_next_direction = -1;

	public int P2_direction = -1;
	public int P2_next_direction = -1;

	public int P3_direction = -1;
	public int P3_next_direction = -1;

	public int P4_direction = -1;
	public int P4_next_direction = -1;
	
	private long autoTime = 0;

	ArrayList<Snake> playerList = new ArrayList<>();
	ArrayList<Integer> directionList = new ArrayList<>();
	ArrayList<Integer> next_directionList = new ArrayList<>();

	private Entity[][] gameBoard = null;
	private int height = 1000;
	private int width = 1000;
	private int gameSize = 100;
	private int totalFood = 5;

	public long speed = 80;
	private Frame frame = null;
	private Canvas canvas = null;
	private Graphics graph = null;
	private BufferStrategy strategy = null;
	public boolean game_over = false;
	public boolean paused = false;

	public long cycleTime = 0;
	public long sleepTime = 0;

	
	public void createSnake(Client client) {
		//playerList holds current players for the gameBoard to see
		Snake player = new Snake(this, "Player" + client.Id, client);

		playerList.add(player);
		directionList.add(-1);
		next_directionList.add(-1);

		System.out.println("Created new Snake" + player.toString());
	}

	public Entity[][] getGameBoard() {
		return gameBoard;
	}

	public void placeEntity(Entity en,int x,int y){
		gameBoard[x][y] = en;
	}

	public int getGameSize() {
		return gameSize;
	}

	public Game() {
		super();
		frame = new Frame();
		canvas = new Canvas();
		
		//Creating new gameboard of entities (Snake, Food, Empty)
		gameBoard = new Entity[gameSize][gameSize]; 

	}

	public void init() {

		frame.setSize(width + 7, height + 27);
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 7, height + 27);
		frame.add(canvas);
		canvas.addKeyListener(this);
		frame.addWindowListener(this);
		frame.dispose();
		frame.validate();
		frame.setTitle("Snake");
		frame.setVisible(true);

		canvas.setIgnoreRepaint(true);
		canvas.setBackground(Color.WHITE);

		canvas.createBufferStrategy(2);

		strategy = canvas.getBufferStrategy();
		graph = strategy.getDrawGraphics();

		initGame();

		renderGame();
	}

	private void initGame(){
		// Create an empty board
		for(int i = 0; i < gameSize; i++){
			for (int j = 0; j < gameSize; j++) {
				//Filling the gameBoard with entities (Empty spaces)
				gameBoard[i][j] = new Entity(this);
			}
		}
		//createSnake();
		for(int i = 0; i< totalFood;i++){
			//Creating food
			createFood();
		}

	}

	public void renderGame() {
		int gridUnit = height / gameSize;
		canvas.paint(graph);

		do {
			do {
				graph = strategy.getDrawGraphics();
				((Graphics2D)graph).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Draw Background
				graph.setColor(Color.WHITE);
				graph.fillRect(0, 0, width, height);

				// Draw snake, bonus ...

				Entity gridCase = new Entity();
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						gridCase = gameBoard[i][j];



						switch (gridCase.getType()){
						case SNAKE :

							graph.setColor(gridCase.getColour());
							graph.fillRect(i * gridUnit, j * gridUnit,
									gridUnit, gridUnit);
							break;

						case FOOD :
							//Implement later
							graph.setColor(Color.CYAN);
							graph.fillOval(i * gridUnit, j * gridUnit,
									gridUnit, gridUnit);
							break;
						default:
							break;
						}
					}
				}
				graph.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 80));

				if (game_over) {
					graph.setColor(Color.RED);
					graph.drawString("GAME OVER", 280, height / 2);
				}
				else if (paused) {
					graph.setColor(Color.BLACK);
					graph.drawString("PAUSED", 350, height / 2);
				}

				//graph.setColor(Color.BLACK);
				//graph.drawString("SCORE = " + getScore(), 10, 20);

				graph.dispose();

			} while (strategy.contentsRestored());
			// Draw image from buffer
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());


	}

	/**
	 * 
	 * @param snake: the snake to move
	 * @param direction current moveing direction
	 * @param next_direction next moving direction
	 */
	public void moveSnake(Snake snake, int direction, int next_direction) {

		if(direction < 0) {
			return;
		}

		int ymove =0;
		int xmove =0;
		switch (direction) {
		case UP:
			xmove = 0;
			ymove = -1;
			break;
		case DOWN:
			xmove = 0;
			ymove = 1;
			break;
		case RIGHT:
			xmove = 1;
			ymove = 0;
			break;
		case LEFT:
			xmove = -1;
			ymove = 0;
			break;
		default:
			xmove = 0;
			ymove = 0;
			break;
		}

		//set current head pos of snake
		int tempx = snake.getPosition()[0][0];
		int tempy = snake.getPosition()[0][1];

		//determine future head pos of snake
		int fut_x = snake.getPosition()[0][0] + xmove;
		int fut_y = snake.getPosition()[0][1] + ymove;

		//wrap around
		if(fut_x < 0)
			fut_x = gameSize - 1;
		if(fut_y < 0)
			fut_y = gameSize - 1;
		if(fut_x >= gameSize)
			fut_x = 0;
		if(fut_y >= gameSize)
			fut_y = 0;

		//Food pickup
		if(gameBoard[fut_x][fut_y].getType()==Type.FOOD){
			snake.setGrow(snake.getGrow()+1);
			createFood();
		}

		snake.setPositionX(0, fut_x);
		snake.setPositionY(0, fut_y);

		//At this point the gameBoard hasn't been updated to hold the position of the moved snake,
		//so we can check if it has collided with anything.

		//This is the entity at the snake's new position
		Entity ent = gameBoard[fut_x][fut_y];
		if (ent.getType() == Type.SNAKE) {
			Snake snek = (Snake)ent;
			//Only check collisions on active snakes
			if (snek.getClient().getActive()) {
				if (snake.getID().equals(snek.getID())) {
					//if the entity at the snake's new position is a snake with the same ID as our snake,
					//our snake ran into itself so it needs to die
					snek.getClient().setActive(false);
					snek.Entity();

					System.out.println("Snake Collision! (" + snake.toString() + ") ran into itself!");
				} else {
					//if the snakes have different IDs, our snake ran into a different snake,
					//so now they both die. Good work snake.
					snake.getClient().setActive(false);
					snake.Entity();
					snek.getClient().setActive(false);
					snek.Entity();
					System.out.println("Snake Collision! (" + snake.toString() + ") ran into (" + snek.toString() + ")!");
				}
			}

			//Check if there's any snakes alive to determine if it's game over
			boolean over = true;
			for (Snake s : playerList) {
				if (s.getClient().getActive()) {
					over = false;
					//We've found at least one active player, so it's not game over!
					break;
				}
			}

			if (over) {
				gameOver();
			}
		}

		gameBoard[tempx][tempy] = new Entity(this);

		int snakex, snakey, i;

		for (i = 1; i < gameSize * gameSize; i++) {
			if ((snake.getPosition()[i][0] < 0) || (snake.getPosition()[i][1] < 0)) {
				break;
			}
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = new Entity(this);
			snakex = snake.getPosition()[i][0];
			snakey = snake.getPosition()[i][1];
			snake.setPositionX(i, tempx);
			snake.setPositionY(i, tempy);
			tempx = snakex;
			tempy = snakey;
		}

		gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]] = snake;
		for (i = 1; i < gameSize * gameSize; i++) {
			if ((snake.getPosition()[i][0] < 0) || (snake.getPosition()[i][1] < 0)) {
				break;
			}
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = snake;
		}



		if (snake.getGrow() > 0) {
			snake.incrementSize();
			snake.setPositionX(i, tempx);
			snake.setPositionY(i, tempy);
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = snake;
			snake.setGrow(snake.getGrow()-1);
		}
	}


	private void createFood() {
		//Find random empty space on gameBoard and put food there
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (gameBoard[x][y].getType() == Type.EMPTY) {
			gameBoard[x][y] = new Food();
		} else {
			createFood();
		}
	}


	private void gameOver() {
		game_over = true;
	}


	// / IMPLEMENTED FUNCTIONS
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		Dimension dim;
		//System.out.println("Key pressed" + ke.toString());

		//Unpauses game if game is paused
		paused = false;

		//System.out.println("P1 Direction: "+P1_direction);

		switch (code) {
		//p1 movement
		case KeyEvent.VK_UP:
			if (P1_direction != DOWN) {
				P1_next_direction = UP;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (P1_direction != UP) {
				P1_next_direction = DOWN;
			}
			break;
		case KeyEvent.VK_LEFT:
			if (P1_direction != RIGHT) {
				P1_next_direction = LEFT;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (P1_direction != LEFT) {
				P1_next_direction = RIGHT;
			}
			break;

			//p2 movement
		case KeyEvent.VK_W:
			if (P2_direction != DOWN) {
				P2_next_direction = UP;
			}break;
		case KeyEvent.VK_S:
			if (P2_direction != UP) {
				P2_next_direction = DOWN;
			}break;
		case KeyEvent.VK_A:
			if (P2_direction != RIGHT) {
				P2_next_direction = LEFT;
			}break;
		case KeyEvent.VK_D:
			if (P2_direction != LEFT) {
				P2_next_direction = RIGHT;
			}break;  

			//p3 movement
		case KeyEvent.VK_NUMPAD8:
			if (P3_direction != DOWN) {
				P3_next_direction = UP;
			}break;
		case KeyEvent.VK_NUMPAD2:
			if (P3_direction != UP) {
				P3_next_direction = DOWN;
			}break;
		case KeyEvent.VK_NUMPAD4:
			if (P3_direction != RIGHT) {
				P3_next_direction = LEFT;
			}break;
		case KeyEvent.VK_NUMPAD6:
			if (P3_direction != LEFT) {
				P3_next_direction = RIGHT;
			}break;	

			//p4 movement
		case KeyEvent.VK_I:
			if (P4_direction != DOWN) {
				P4_next_direction = UP;
			}break;
		case KeyEvent.VK_K:
			if (P4_direction != UP) {
				P4_next_direction = DOWN;
			}break;
		case KeyEvent.VK_J:
			if (P4_direction != RIGHT) {
				P4_next_direction = LEFT;
			}break;
		case KeyEvent.VK_L:
			if (P4_direction != LEFT) {
				P4_next_direction = RIGHT;
			}break;		



		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;

		case KeyEvent.VK_SPACE:
			if(!game_over)
				paused = true;
			break;

		/*case KeyEvent.VK_INSERT:
			if(!game_over){}
			System.out.println("Game-Debug: creating non-cliented snake");
			//Adds a snake to the board at a random position
			//Snake snake = new Snake();
			createSnake();
			break;*/
		default:
			// Unsupported key
			break;
		}
	}
	
	public void randomDirection(int i) {
		autoTime += 1;
		if (autoTime > 10) {
			Random rand = new Random();
			int value = rand.nextInt(4);
			
			if (value == 0) {
				if (directionList.get(i) != DOWN) {
					next_directionList.set(i, UP);
				}
			}
			else if (value == 1) {
				if (directionList.get(i) != UP) {
					next_directionList.set(i, DOWN);
				}
			}
			else if (value == 2) {
				if (directionList.get(i) != RIGHT) {
					next_directionList.set(i, LEFT);
				}
			}
			else if (value == 3) {
				if (directionList.get(i) != LEFT) {
					next_directionList.set(i, RIGHT);
				}
			}
			autoTime = 0;
		}
	}

	public void windowClosing(WindowEvent we) {
		System.exit(0);
	}




	// UNNUSED IMPLEMENTED FUNCTIONS
	public void keyTyped(KeyEvent ke) {}
	public void keyReleased(KeyEvent ke) {}
	public void windowOpened(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowActivated(WindowEvent we) {}
	public void windowDeactivated(WindowEvent we) {}


}
