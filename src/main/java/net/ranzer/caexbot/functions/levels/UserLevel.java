package net.ranzer.caexbot.functions.levels;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class UserLevel implements Comparable<UserLevel> {

	final private int experience;
	final private Member member;
	final private long lastXP;
	
	/**
	 * create new UserLevel with initial Experience
	 */
	public UserLevel(Member m, int XP, Long lxp) {
		experience = XP;
		member=m;
		lastXP = lxp;
	}
	
	public UserLevel(Member m, int XP) {
		experience = XP;
		member=m;
		lastXP = System.currentTimeMillis();
	}
	

	public int getXP() {
		return experience;
	}

	public int getLevel() {
		return getLevel(getXP());
	}
	
	public long getLastXPTime() {
		return lastXP;
	}

	public Member getMember(){
		return member;
	}
	
	public User getUser() {
		return member.getUser();
	}

	public Guild getGuild() {
		return member.getGuild();
	}

	public static int getLevel(int xp){
		//base lvl->xp formula => XP -> level formula
		//(500*x^2)-(500*x) => 1/50 (25 + sqrt(5) sqrt(125 + x))

		return (int) Math.floor((25+Math.sqrt(5)*Math.sqrt(125+xp))/50);

	}
	
	static public int calcXPForLevel(int level){
				
		return (int)Math.floor((500*Math.pow(level,2))-(500*level));
		
	}

	@Override
	public int compareTo(UserLevel o) {

		return Integer.compare(o.getXP(), this.getXP());

	}
}
