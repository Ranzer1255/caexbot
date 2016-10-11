package caexbot.functions.games.zdice;

/* 
 * this class will be the unit that takes in commands from Discord
 * and passes them to the Game. takes the responces from the game and interperets
 * it into output via Discord
 * 
 * i may make discord integration into a subclass and make this abstract
 * 
 * (in MVC terms this is the Controler, while the discord Command structure is the View)
 */
public abstract class ZomDiceControler {

	private ZomDicePlayerList players = new ZomDicePlayerList();

	/**
	 * starts the game if players contains more than one participant. 
	 */
	public abstract void startGame();
	
	public void addPlayer(Player p){
		players.addPlayer(p);
	}

	public void removePlayer(Player p){
		players.removePlayer(p);
	}
	
	
	
}
