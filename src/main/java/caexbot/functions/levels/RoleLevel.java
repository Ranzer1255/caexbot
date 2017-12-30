package caexbot.functions.levels;

import net.dv8tion.jda.core.entities.Role;

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
		if(this.xp>o.getXp()) return -1;
		if(this.xp<o.getXp()) return 1;
		return 0;
	}
	
	

}
