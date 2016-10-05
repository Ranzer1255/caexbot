package caexbot.functions.levels;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import caexbot.database.CaexDB;
import caexbot.util.Logging;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public class expTable {

	private static expTable instance;
	private Map<Pair<Guild,User>, UserLevel> exp;
	
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
		
		Pair<Guild,User> key = new ImmutablePair<>(guild, author);
		
		if(!exp.containsKey(key)){
			UserLevel u = new UserLevel(XP);
			exp.put(key, u);
			CaexDB.addRow(key,u);
			return;
		}
		
		exp.get(key).addXP(XP);
		CaexDB.addXP(key,XP);
		
//		save.save(this);
		
	}
	
	
	public void load(){
		exp.putAll(CaexDB.getLevels());
	}

	public int getXP(Guild guild, User author) {
		// TODO Auto-generated method stub
		return exp.get(new ImmutablePair<Guild, User>(guild, author)).getXP();
	}
}
