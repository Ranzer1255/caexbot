package caexbot.config;

import java.util.Map;

import caexbot.database.CaexDB;
import net.dv8tion.jda.core.entities.Guild;

/**
 * container for all the guild data objects
 * @author Ranzer
 *
 */
public class GuildManager {
	
	private static Map<Guild, GuildData> guildData;

//	private static Map<Guild, String> prefixes = CaexDB.loadPrefixes();
	
	public static GuildData getGuildData(Guild key){
		
		if(!guildData.containsKey(key)){
			buildGuildData(key);
		}
		
		return guildData.get(key);
	}

	//convenance passthrough methods
	public static String getPrefix(Guild key){
		if (guildData.get(key).getPrefix()==null){
			return CaexConfiguration.getInstance().getPrefix();
		}
		return guildData.get(key).getPrefix();
	}
	
	public static void setPrefix(Guild key, String prefix){
		guildData.get(key).setPrefix(prefix);
	}
	
	public static void removePrefix(Guild key){
		guildData.get(key).removePrefix();
		CaexDB.removePrefix(key);
		
	}
	
	private static void buildGuildData(Guild guild) {
		//TODO save to DB here? or in GuildData?
		guildData.put(guild, new GuildData(guild));
	}

	private static void load(){
		//TODO load from DB
	}
}
