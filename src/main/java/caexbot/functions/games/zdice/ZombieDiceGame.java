package caexbot.functions.games.zdice;

import caexbot.functions.games.zdice.controlers.ZomDiceController;

public class ZombieDiceGame {
	
	private enum State{
		PRE_GAME,PLAYING
	}
	
	private ZomDiceController controller; 
	private ZomDicePlayerList players;
	private ZomDicePool dicePool;
	private State state;

	public ZombieDiceGame(){
		dicePool = new ZomDicePool();
		players = new ZomDicePlayerList();
		state = State.PRE_GAME;
	}
	
	public void addPlayer(Player p){
		players.addPlayer(p);
	}

	public void removePlayer(Player p){
		players.removePlayer(p);
	}

	public void start() {
		if (state==State.PRE_GAME) {
			this.state = State.PLAYING;
			controller.announceGameStart();
			controller.promptPlayer(players.nextPlayer());
		}
	}

	public void setController(ZomDiceController controller) {
		this.controller=controller;
	}
	

}
