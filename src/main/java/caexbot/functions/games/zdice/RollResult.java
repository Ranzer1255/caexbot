package caexbot.functions.games.zdice;

import java.util.ArrayList;
import java.util.List;

import caexbot.functions.games.zdice.ZomDie.Side;

public class RollResult {
	
	private List<ZomDie> diceRolled;
	private List<Side> sideResults = new ArrayList<>();
	private int points,shots;

	public void diceRolled(List<ZomDie> hand) {
		diceRolled=hand;
		
	}

	public void addDieResult(Side result) {
		sideResults.add(result);
		
	}

	public void addTotals(int pointsThisTurn, int shots) {
		points=pointsThisTurn; this.shots=shots;
		
	}
	
	public List<ZomDie> getDiceRolled() {
		return diceRolled;
	}
	
	public List<Side> getSideResults() {
		return sideResults;
	}
	
	public int getPoints() {
		return points;
	}
	public int getShots() {
		return shots;
	}

}
