package caexbot.data;

import java.util.HashMap;
import java.util.Map;

import caexbot.config.CaexConfiguration;
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
	
	private static Map<Guild, GuildData> guildData = new HashMap<>();

//	private static Map<Guild, String> prefixes = CaexDB.loadPrefixes();
	
	public static GuildData getGuildData(Guild key){
		
		if(!guildData.containsKey(key)){
			buildGuildData(key);
		}
		
		return guildData.get(key);
	}

	private static void buildGuildData(Guild guild) {
		guildData.put(guild, new GuildData(guild));
	}

	//convenance passthrough methods
	public static String getPrefix(Guild key){

		return getGuildData(key).getPrefix();
	}
	
	public static void setPrefix(Guild key, String prefix){
		getGuildData(key).setPrefix(prefix);
	}
	
	public static void removePrefix(Guild key){
		getGuildData(key).removePrefix();
		CaexDB.removePrefix(key);
		
	}
	
}
