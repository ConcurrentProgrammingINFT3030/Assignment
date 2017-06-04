package SnakeGame;

public class MoveData {

	private int direction;
	private int Id;
	
	public MoveData(int theDirection, int theId)
	{
		this.direction = theDirection;
		this.Id = theId;
	}
	
	public int getDirection()
	{
		return direction;
	}
	public int getId()
	{
		return Id;
	}
	@Override
	public String toString()
	{
		return "Client: " + Id + ", Direction: " + direction;
	}
}
