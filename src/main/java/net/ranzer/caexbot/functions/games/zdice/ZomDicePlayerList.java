package net.ranzer.caexbot.functions.games.zdice;

import java.util.Deque;
import java.util.LinkedList;

/**
 * handles the list of players and tracks the current player.
 * @author Ranzer
 *
 */
public class ZomDicePlayerList {
	private Deque<Player> players = new LinkedList<>();
	private Player currentPlayer;
	
	/**
	 * adds player to the queue if they are not already in it.
	 * @param player to be added to Queue
	 * @return true if player was added
	 */
	public boolean addPlayer(Player player){
		if (!players.contains(player)) {
			return players.add(player);
		}
		return false;
	}
	
	/**
	 * removes a player from the Queue
	 * @param player to be removed.
	 */
	public boolean removePlayer(Player player){
		return players.remove(player);
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
	
	public Deque<Player> getPlayerList(){
		return players;
	}
	
	public int numberOfPlayers(){
		return players.size();
	}

	public void clear() {
		players.clear();
		currentPlayer = null;
	}

	public Player getPlayer(Player p) {
		return ((LinkedList<Player>) players).get(((LinkedList<Player>) players).indexOf(p));
	}
}