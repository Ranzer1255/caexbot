package caexbot.functions.games.zdice;

import caexbot.functions.games.zdice.controlers.ZomDiceController;

public class ZombieDiceGame {
	
	private enum State{
		PRE_GAME,PLAYING, FINAL_ROUND
	}

	public static final int WINNING_SCORE = 13;
	public static final int SHOTS_TO_END_TURN = 3;
	public static final int MIN_PLAYERS = 2;
	
	private ZomDiceController controller; 
	private ZomDicePlayerList players;
	private Player activePlayer;
	private Turn turn;
	private State state;

	//final round fields
	private Player gameEnder;// used to keep track of who ended the game for the final round.
	private Player highScore;

	public ZombieDiceGame(){
		players = new ZomDicePlayerList();
		turn = new Turn();
		state = State.PRE_GAME;
	}
	
	public void setController(ZomDiceController controller) {
		this.controller=controller;
	}

	public boolean addPlayer(Player p){
		return players.addPlayer(p);
	}

	public boolean removePlayer(Player p){
		if (p.equals(activePlayer)){
			startNextTurn();// may cause issues if player leaves during final round.
		}
		return players.removePlayer(p);
	}

	/**
	 * 
	 * @return true if the game was successfully started
	 */
	public boolean start() {
		if (state==State.PRE_GAME) {
			if (players.getPlayerList().size()>=MIN_PLAYERS) {
				this.state = State.PLAYING;
				controller.announceGameStart(players.getPlayerList());
				startNextTurn();
				return true;
			} else {
				controller.needMorePlayers();
				return false;
			}
		}
		return false;
	}
	
	private void setActivePlayer(Player currentPlayer) {
		activePlayer = currentPlayer;
		controller.promptPlayer(activePlayer);
		
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
		controller.announceEndTurn(activePlayer);
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

	public void startNextTurn() {
		setActivePlayer(players.nextPlayer());
		turn.startTurn(activePlayer);
	}
	
	private void startFinalRound() {
		state=State.FINAL_ROUND;
		gameEnder = activePlayer;
		highScore = gameEnder;
		controller.announceFinalRound();
		startNextFinalTurn();
	}

	private void startNextFinalTurn() {
		if(activePlayer.getBrains()>highScore.getBrains()){
			highScore = activePlayer;
		}
		
		players.nextPlayer();
		if(players.getCurrentPlayer().equals(gameEnder)){
			endGame();
		} else{
			setActivePlayer(players.getCurrentPlayer());
			turn.startTurn(activePlayer);
		}
		
	}

	private void endGame() {
		controller.announceGameEnd(
				players.getPlayerList().stream()
					.sorted((p1, p2) -> p1.compare(p2)).toArray(Player[]::new));
		players.clear();
		state= State.PRE_GAME;
	}

	public Player getPlayer(Player p) {
		return players.getPlayer(p);
	}

	
	

}
