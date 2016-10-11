package caexbot.functions.games.zdice;

import java.util.LinkedList;
import java.util.List;

/**
 * handles the list of players and tracks the current player.
 * @author jrdillingham
 *
 */
public class ZomDicePlayerList {
	private LinkedList<Player> players = new LinkedList<>();
	private Player currentPlayer;
	
	/**
	 * adds player to the queue if they are not already in it.
	 * @param player to be added to Queue
	 * @return true if player was added
	 */
	public boolean addPlayer(Player player){
		if (!players.contains(player)) {
			players.add(player);
			return true;
		}
		return false;
	}
	
	/**
	 * removes a player from the Queue
	 * @param player to be removed.
	 */
	public void removePlayer(Player player){
		players.remove(player);
	}
	
	/**
	 * moves to the next player in the queue. also sets current player to be equal to Player returned.
	 * 
	 * @return next player in the queue
	 */
	public Player nextPlayer(){
		Player rtn = players.remove();
		players.add(rtn);
		currentPlayer = rtn;
		return rtn;	
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	
	public List<Player> getPlayerList(){
		return players;
	}
	
	public int numberOfPlayers(){
		return players.size();
	}
}