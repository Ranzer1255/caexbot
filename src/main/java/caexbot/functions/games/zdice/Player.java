package caexbot.functions.games.zdice;

public abstract class Player {
	private int points;
	
	public Player(){
		points=0;
		
	}
	
	public void addBrains(int p){
		points += p;
	}

	public int getBrains(){
		return points;
	}

	

}
