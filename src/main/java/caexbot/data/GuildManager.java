package caexbot.data;

import java.util.HashMap;
import java.util.Map;

import caexbot.database.CaexDB;
import net.dv8tion.jda.core.entities.Guild;

/**
 * container for all the guild data objects
 * may have a conflict with JDA GuildManager... we'll see
 * 
 * @author Ranzer
 *
 */
public class GuildManager {
	
	public static GuildData getGuildData(Guild key){
		return new GuildData(key);
	}

	//convenance passthrough methods
	public static String getPrefix(Guild key){
		return new GuildData(key).getPrefix();
	}
	
	public static void setPrefix(Guild key, String prefix){
		new GuildData(key).setPrefix(prefix);
	}
	
	public static void removePrefix(Guild key){
		new GuildData(key).removePrefix();
	}
	
}


