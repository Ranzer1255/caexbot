package caexbot.functions.games.zdice;

import caexbot.functions.games.zdice.controlers.ZomDiceController;

public class ZombieDiceGame {
	
	private enum State{
		PRE_GAME,PLAYING
	}
	
	private ZomDiceController controller; 
	private ZomDicePlayerList players;
	private Player activePlayer;
	private Turn turn;
	private State state;

	public ZombieDiceGame(){
		players = new ZomDicePlayerList();
		turn = new Turn();
		state = State.PRE_GAME;
	}
	
	public void setController(ZomDiceController controller) {
		this.controller=controller;
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
			nextPlayer();
		}
	}
	
	public void roll(Player p){
		if(state==State.PRE_GAME) return;
		
		if(!p.equals(activePlayer)){
			controller.notActivePlayer();
			return;
		}
		
		controller.rollResult(turn.roll());
		
	}

	private void nextPlayer() {
		turn.startTurn(players.nextPlayer());
		controller.promptPlayer(players.getCurrentPlayer());
	}
	
	

}
