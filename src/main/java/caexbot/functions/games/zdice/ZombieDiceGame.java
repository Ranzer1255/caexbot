package caexbot.functions.games.zdice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ZombieDiceGame {

	private static final int NUM_GREEN = 6;
	private static final int NUM_YELLOW = 4;
	private static final int NUM_RED = 3;
	private static ZombieDiceGame instance;
	
	private List<ZomDie> dicePool;
	
	private ZombieDiceGame(){
		dicePool = buildDicePool();
		
	}
	
	public static ZombieDiceGame getGame(){
		
		if (instance==null){
			instance = new ZombieDiceGame();
		}
		
		return instance;
	}
	
	/*
	 * the probability curve of this logic will be slightly off from the physical counterpart.
	 * however i don't see it drastically effecting the game.
	 */
	public List<ZomDie> getDiceFromPool(int num){
		List<ZomDie> rtn = new ArrayList<>();
		
		
		for (int i = 0; i < num; i++) {
			
			if (dicePool.isEmpty()) {
				rebuildPool();
			}
			rtn.add(dicePool.remove(ThreadLocalRandom.current().nextInt(dicePool.size())));
		}
		return rtn;
	}

	private List<ZomDie> buildDicePool() {
		LinkedList<ZomDie> rtn = new LinkedList<ZomDie>();
		
		for (int i = 0; i < NUM_GREEN ; i++) {
			rtn.add(new ZomDie(ZomDie.Color.GREEN));
		}
		for (int i = 0; i < NUM_YELLOW ; i++) {
			rtn.add(new ZomDie(ZomDie.Color.YELLOW));
		}
		for (int i = 0; i < NUM_RED ; i++) {
			rtn.add(new ZomDie(ZomDie.Color.RED));
		}
		
		return rtn;
	}

	private void rebuildPool() {
		dicePool = buildDicePool();
		
	}
}
