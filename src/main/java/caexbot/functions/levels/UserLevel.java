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
	}

	public void addXP(int XP) {
		experience += XP;
		
	}

	public int getXP() {
		return experience;
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
