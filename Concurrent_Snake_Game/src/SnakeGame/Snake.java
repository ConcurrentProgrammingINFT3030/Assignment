package SnakeGame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import SnakeGame.Entity.Type;


/**
 * 
 * @author JM
 *
 */
public class Snake extends Entity implements Runnable{
	
	@Override
	public void run() {
		try{
			System.out.println("Running");
		}
		catch(Exception e){
			System.out.println("Error");
		}
	}
	
	private int[][] position = null;
	private int size = 0;
	private int grow = 0;
	private Color colour;
	private Type type;
	private Game game;
	private String id;
	
	
	public Snake(Game game, String id){
		this.game = game;
		this.id = id;
		type = Type.SNAKE;
		size = 1;
		grow = 0;
		//makes snake random colour
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		colour = new Color(r, g, b);
		
		//Place in random position
		position = new int[game.getGameSize() * game.getGameSize()][2];
		
		for (int i = 0; i < game.getGameSize() * game.getGameSize(); i++) {
			position[i][0] = -1;
			position[i][1] = -1;
		}
		
		int x = (int) (Math.random() * 1000) % game.getGameSize();
        int y = (int) (Math.random() * 1000) % game.getGameSize();
        boolean findEmpty = false;
        while(!findEmpty){
            if (game.getGameBoard()[x][y].getType() == Type.EMPTY) {
            	position[0][0] = x;
        		position[0][1] = y;
        		findEmpty = true;
            } else {
            	x = (int) (Math.random() * 1000) % game.getGameSize();
                y = (int) (Math.random() * 1000) % game.getGameSize();
            }
        }
        game.placeEntity(this, x, y);
	}
	
	
	
	public String getID(){
		return id;
	}
	
	
	public int getGrow(){
		return grow;
	}
	
	public void setGrow(int num){
		grow = num;
	}
	
	public Color getColour(){
		return colour;
	}
	
	public int getSize(){
		return size;
	}
	
	public void incrementSize(){
		size++;
	}
	
	public int[][] getPosition(){
		return position;
	}
	
	public void setPositionX(int segment, int x){
		position[segment][0] = x;
	}
	
	public void setPositionY(int segment, int y){
		position[segment][1] = y;
	}
	
	public Type getType(){
		return type;
	}
	
	public String toString(){
		String returnString = "ID: "+ id+" HeadLocation: ("+position[0][0]+","+position[0][1]+") size: "+size;
		return returnString;
	}
	public void Entity() {
		type = Type.EMPTY;
	}
}
