package net.ranzer.caexbot.functions.levels;

import net.dv8tion.jda.api.entities.Role;

public class RoleLevel implements Comparable<RoleLevel> {

	public final Role ROLE;
	public final String ROLENAME;
	private int xp = 0;
	
	public RoleLevel(Role role){
		ROLENAME = role.getName();
		ROLE = role;
	}
	
	
	public void addXp(int memberXp){
		xp+=memberXp;
	}
	
	public int getXp(){
		return xp;
	}
	
	@Override
	public int compareTo(RoleLevel o) {
		return Integer.compare(o.getXp(), this.xp);
	}
	
	

}
