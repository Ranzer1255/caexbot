package caexbot.functions.levels;

import java.util.HashMap;
import java.util.Map;

import caexbot.database.CaexDB;
import caexbot.util.Logging;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public class expTable {

	private static expTable instance;
	private Map<Guild, Map<User, UserLevel>> exp;
	
	private expTable(){
		exp = new HashMap<>();
		load();
	}
	
	public static expTable getInstance(){

		if(instance == null){
			instance = new expTable();
		}
		
		return instance;
	}

	public void addXP(Guild guild, User author, int XP) {

		Logging.debug("Adding "+ XP + "XP to "+ author.getUsername()+":"+guild.getName());
		
		if(!exp.containsKey(guild))
			exp.put(guild, new HashMap<>());
		
		Map<User, UserLevel> subMap = exp.get(guild);
		
		if(!subMap.containsKey(author)){
			UserLevel u = new UserLevel(XP);
			subMap.put(author, u);
			CaexDB.addRow(guild,author,u);//TODO fix DB intergration
			return;
		}
			
				
		subMap.get(author).addXP(XP);
		CaexDB.addXP(guild, author,XP);//TODO fix DB intergration
		
	}
		
	private void load(){
//		exp.putAll(CaexDB.getLevels()); TODO fix DB integration
	}

	public int getXP(Guild guild, User author) {
		// TODO Auto-generated method stub
		return exp.get(guild).get(author).getXP();
	}
}
