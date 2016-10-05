package caexbot.functions.levels;

public class UserLevel {

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
}
