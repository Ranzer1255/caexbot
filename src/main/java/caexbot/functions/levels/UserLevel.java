package caexbot.functions.levels;

import java.util.ArrayList;

public class UserLevel implements Comparable<UserLevel> {

	private static final int NUM_LEVELS = 20;
	private int experience;
	private int level;
	private static ArrayList<Integer> levels= new ArrayList<>();
	
	static{
		for (int i = 0; i < NUM_LEVELS+1; i++) {
			if (i==0)
				levels.add(0);
			else{
				levels.add(levels.get(i-1)+((i)*1000));
			}
		}
		System.out.println(levels.toString());
	}
	/**
	 * create new UserLevel with initial Expereience
	 * @param XP
	 */
	public UserLevel(int XP) {
		experience = XP;
		
		for (int i=0;i<levels.size();i++) {
			if(levels.get(i).intValue()<experience)
				continue;
			if(levels.get(i).intValue()>=experience){
				this.level = i;
				break;
			}
		}
	}

	public boolean addXP(int XP) {
		experience += XP;
		if(experience>levels.get(level)){
			level++;
			return true;
		}
		return false;
	}

	public int getXP() {
		return experience;
	}

	public int getLevel() {
		return level;
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
