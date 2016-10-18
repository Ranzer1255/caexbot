package caexbot.functions.games.zdice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ZomDie {
	
	private final Color color;

	public ZomDie(Color color) {
		this.color=color;
	}

	public enum Color {
		GREEN(3,1,2),
		YELLOW(2,2,2),
		RED(1,3,2);
		
		public int brains,runs,shots;
		
		Color(int B, int S, int R){
			brains = B; runs = R; shots=S;
		}
	}
	
	public enum Side {
		BRAIN,RUN,SHOT
	}
	
	/**
	 * roll's the die
	 * 
	 * @return Side that the die rolled
	 */
	public Side roll(){
		List<Side> rtn = new ArrayList<>();
		
		for (int i = 0; i < color.brains; i++) {
			rtn.add(Side.BRAIN);
		}
		for (int i = 0; i < color.runs; i++) {
			rtn.add(Side.RUN);
		}
		for (int i = 0; i < color.shots; i++) {
			rtn.add(Side.SHOT);
		}
		return rtn.get(ThreadLocalRandom.current().nextInt(rtn.size()));
		
	}
	
	public Color getColor(){
		return color;
	}
}
