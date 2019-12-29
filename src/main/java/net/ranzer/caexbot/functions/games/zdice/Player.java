package net.ranzer.caexbot.functions.games.zdice;

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

	public int compare(Player p2) {
		if (this.getBrains()==p2.getBrains())
			return 0;
		if (this.getBrains()>p2.getBrains())
			return -1;
		else 
			return 1;
	}

	

}
