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
		GREEN(3,1,2,"Green"),
		YELLOW(2,2,2,"Yellow"),
		RED(1,3,2,"Red");
		
		public int brains,runs,shots;
		public String name;
		
		Color(int B, int S, int R, String n){
			brains = B; runs = R; shots=S; name=n; 
		}
	}
	
	public enum Side {
		BRAIN("Brain"),
		RUN("Run"),
		SHOT("Shot");
		
		public String name;
		Side(String name){
			this.name=name;
		}
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
