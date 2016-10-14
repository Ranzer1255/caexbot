package caexbot.functions.games.zdice;

import java.util.Iterator;

import caexbot.functions.games.zdice.controlers.ZomDiceController;

public class ZombieDiceGame {
	
	private enum State{
		PRE_GAME,PLAYING, FINAL_ROUND
	}

	private static final int WINNING_SCORE = 13;

	private static final int SHOTS_TO_END_TURN = 3;
	
	private ZomDiceController controller; 
	private ZomDicePlayerList players;
	private Player activePlayer;
	private Turn turn;
	private State state;

	//final round fields
	private Player gameEnder;// used to keep track of who ended the game for the final round.
	private Player highScore;
	private int highBrains;

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
			startNextTurn();
		}
	}
	
	public void roll(Player p){
		if(state==State.PRE_GAME) return;
		
		if(!p.equals(activePlayer)){
			controller.notActivePlayer();
			return;
		}
		RollResult result = turn.roll();
		controller.rollResult(result);
		if(result.getShots()>=SHOTS_TO_END_TURN)
			endTurn();
		
	}
	
	public void endTurn(){
		turn.endTurn();
		controller.endTurn(activePlayer);
		if(state==State.FINAL_ROUND) {
			startNextFinalTurn();
			return;
		}
		if(activePlayer.getBrains()>= WINNING_SCORE){
			startFinalRound();
			return;
		}
		startNextTurn();
	}

	private void startNextFinalTurn() {
		// TODO Final Round next player logic.
		
	}

	public void startNextTurn() {
		turn.startTurn(players.nextPlayer());
		setActivePlayer(players.getCurrentPlayer());
	}
	
	private void setActivePlayer(Player currentPlayer) {
		activePlayer = currentPlayer;
		controller.promptPlayer(activePlayer);
		
	}

	private void startFinalRound() {
		// TODO Final Round of Game-play
		state=State.FINAL_ROUND;
		gameEnder = activePlayer;
		highScore = gameEnder;
		highBrains = gameEnder.getBrains();
		
	}

	
	

}
