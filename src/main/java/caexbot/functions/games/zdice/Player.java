package caexbot.functions.games.zdice;

public abstract class Player {
	private int points;
	
	public Player(){
		points=0;
		
	}
	
	public void addPoints(int p){
		points += p;
	}

	public int getPoints(){
		return points;
	}

	

}
