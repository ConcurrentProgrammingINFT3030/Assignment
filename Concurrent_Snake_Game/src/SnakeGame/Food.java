package SnakeGame;


/**
 * 
 * @author JM
 *
 */
public class Food extends Entity {

	private Type type;
	
	
	public Food(){

		type = Type.FOOD;
	}
	
	public Type getType(){
		return type;
	}
	
}
