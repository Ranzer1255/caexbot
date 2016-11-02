package caexbot.functions.games.zdice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Turn {

	public final static int HAND_SIZE = 3;
	private List<ZomDie> hand;
	private Player activePlayer;
	private ZomDicePool dicePool;
	
	private int brains;
	private int shots;
	
	public Turn(){
		dicePool = new ZomDicePool();
		hand=new ArrayList<>();
	}
	
	public void startTurn(Player p){
		activePlayer=p;
		hand.clear();
		dicePool.reset();
		brains = 0;
		shots=0;
	}
	
	private void drawHand() {
		
		hand.addAll(dicePool.getDiceFromPool(getNeeded()));
	}
	
	private int getNeeded() {
		return HAND_SIZE-hand.size();
	}

	public RollResult roll() {
		
		RollResult rtn = new RollResult();
		
		drawHand();
		rtn.diceRolled(new ArrayList<>(hand));
		Iterator<ZomDie> i = hand.iterator();
		
		while (i.hasNext()){
			ZomDie die = i.next();
			ZomDie.Side result = die.roll();
			rtn.addDieResult(result);
			switch (result){
			case BRAIN:
				brains++;
				i.remove();
				break;
			case RUN:
				break;
			case SHOT:
				shots++;
				i.remove();
				break;
			}
			rtn.addTotals(brains,shots);
		}
		return rtn;
	}

	public void endTurn() {
		if(shots < ZombieDiceGame.SHOTS_TO_END_TURN)
			activePlayer.addBrains(brains);
		else 
			activePlayer.addBrains(0);
		activePlayer = null;
	}
}
