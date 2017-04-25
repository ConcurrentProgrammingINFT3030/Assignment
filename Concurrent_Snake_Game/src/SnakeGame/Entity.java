package SnakeGame;

import java.awt.Color;

/**
 * Default Entity type Empty
 * @author JM
 *
 */
public class Entity {

	//Snake, Food, Empty
	private Type type;
	private Game game;
	private Color colour;
	private String id;
	
	public enum Type{
		SNAKE, FOOD, EMPTY
	}
	
	public Entity(Game game){
		type = Type.EMPTY;
		this.game = game;
	}
	
	
	public Entity() {
		type = Type.EMPTY;
	}

	
	public Type getType(){
		return type;
	}
	
	public Color getColour(){
		return colour;
	}
	
	public String getID(){
		return id;
	}
	
	
}
