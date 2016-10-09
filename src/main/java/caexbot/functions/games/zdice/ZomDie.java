package caexbot.functions.games.zdice;


public class ZomDie {
	
	private final Type type;

	public ZomDie(Type type) {
		this.type=type;
	}

	enum Type {
		GREEN(3,1,2),
		YELLOW(2,2,2),
		RED(1,3,2);
		
		public int brains,runs,shots;
		
		Type(int B, int R, int S){
			brains = B; runs = R; shots=S;
		}
	}
}
