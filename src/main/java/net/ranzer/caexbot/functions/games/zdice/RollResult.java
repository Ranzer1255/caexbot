package net.ranzer.caexbot.functions.games.zdice;

import java.util.ArrayList;
import java.util.List;

import net.ranzer.caexbot.functions.games.zdice.ZomDie.Side;

/**
 * a data collection for the controller and view to process to show the result of the die roll.
 * 
 * @author Ranzer
 *
 */
public class RollResult {
	
	private List<ZomDie> diceRolled;
	private List<Side> sideResults = new ArrayList<>();
	private int brains,shots;

	public void diceRolled(List<ZomDie> hand) {
		diceRolled=hand;
		
	}

	public void addDieResult(Side result) {
		sideResults.add(result);
		
	}

	public void addTotals(int brainsThisTurn, int shots) {
		brains=brainsThisTurn; this.shots=shots;
		
	}
	
	public List<ZomDie> getDiceRolled() {
		return diceRolled;
	}
	
	public List<Side> getSideResults() {
		return sideResults;
	}
	
	public int getBrains() {
		return brains;
	}
	public int getShots() {
		return shots;
	}

}
