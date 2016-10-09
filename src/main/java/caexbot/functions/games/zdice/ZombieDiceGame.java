package caexbot.functions.games.zdice;

import java.util.LinkedList;
import java.util.List;

public class ZombieDiceGame {

	private static final int NUM_GREEN = 6;
	private static final int NUM_YELLOW = 4;
	private static final int NUM_RED = 3;
	private static ZombieDiceGame instance;
	
	private List<ZomDie> dicePool;
	
	private ZombieDiceGame(){
		dicePool = buildDicePool();
		
	}
	
	private List<ZomDie> buildDicePool() {
		LinkedList<ZomDie> rtn = new LinkedList<ZomDie>();
		
		for (int i = 0; i < NUM_GREEN ; i++) {
			rtn.add(new ZomDie(ZomDie.Type.GREEN));
		}
		for (int i = 0; i < NUM_YELLOW ; i++) {
			rtn.add(new ZomDie(ZomDie.Type.YELLOW));
		}
		for (int i = 0; i < NUM_RED ; i++) {
			rtn.add(new ZomDie(ZomDie.Type.RED));
		}
		
		return rtn;
	}

	public static ZombieDiceGame getGame(){
		
		if (instance==null){
			instance = new ZombieDiceGame();
		}
		
		return instance;
	}
	
	public ZomDieHand getHand(){
		return null; //TODO pull 3 dice from pool and return as hand
	}
}
