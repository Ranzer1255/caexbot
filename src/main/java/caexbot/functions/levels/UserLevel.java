package caexbot.functions.levels;

public class UserLevel implements Comparable<UserLevel> {

	private int experience;
	private int level;
	
	/**
	 * create new UserLevel with initial Expereience
	 * @param XP
	 */
	public UserLevel(int XP) {
		experience = XP;
		
		level=getLevel(XP);
		
	}

	public boolean addXP(int XP) {
		experience += XP;
		if(getLevel()>level){
			level=getLevel();
			return true;
		}
		return false;
	}

	public int getXP() {
		return experience;
	}

	public int getLevel() {
		return getLevel(getXP());
	}
	
	private int getLevel(int xp){
		int rtn = 1;
		boolean found=false;
		while(!found){
			rtn+=1;
			if (xp<calcXPForLevel(rtn)){
				found=true;
			}
			
		}
		return rtn-1;
	}
	
	public int calcXPForLevel(int level){
		if (level == 1){
			return 0;
		} else {
			return calcXPForLevel(level-1)+((level-1)*1000);
		}
		
	}

	@Override
	public int compareTo(UserLevel o) {
		
		if(this.getXP()<o.getXP())
			return 1;
		if(this.getXP()>o.getXP())
			return -1;
		return 0;
		
	}
}
