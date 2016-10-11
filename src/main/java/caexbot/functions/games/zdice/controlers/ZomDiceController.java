package caexbot.functions.games.zdice.controlers;

import caexbot.functions.games.zdice.Player;
import caexbot.functions.games.zdice.RollResult;
import caexbot.functions.games.zdice.ZombieDiceGame;

/* 
 * this class will be the unit that takes in commands from Discord
 * and passes them to the Game. takes the responses from the game and interprets
 * it into output via Discord
 * 
 * i may make discord integration into a subclass and make this abstract
 * 
 * (in MVC terms this is the Controller, while the discord Command structure is the View)
 */
public abstract class ZomDiceController {

	ZombieDiceGame game;
	
	public ZomDiceController(){
		game = new ZombieDiceGame();
		game.setController(this);
	}

	/**
	 * starts the game if players contains more than one participant. 
	 */
	public void startGame(){
		game.start();
	}
	
	public void addPlayer(Player p){
		game.addPlayer(p);
	}

	public void removePlayer(Player p){
		game.removePlayer(p);
	}

	abstract public void announceGameStart();

	abstract public void promptPlayer(Player nextPlayer);
	
	abstract public void roll(Player p);

	abstract public void notActivePlayer();
	
	abstract public void rollResult(RollResult result);
	
	
	
	
}
