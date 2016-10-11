package caexbot.functions.games.zdice;

import caexbot.functions.games.zdice.controlers.ZomDiceController;

public class ZombieDiceGame {
	
	private ZomDiceController controller; 
	private ZomDicePlayerList players = new ZomDicePlayerList();
	private ZomDicePool dicePool = new ZomDicePool();

	public ZombieDiceGame(){
		
	}
	
	public void addPlayer(Player p){
		players.addPlayer(p);
	}

	public void removePlayer(Player p){
		players.removePlayer(p);
	}

	public void start() {
		// TODO Start game if not already running.
		
	}

	public void setController(ZomDiceController Controller) {
		this.controller=controller;
		
	}
	

}
