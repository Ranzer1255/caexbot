package caexbot.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import caexbot.config.CaexConfiguration;
import caexbot.database.CaexDB;
import caexbot.functions.levels.UserLevel;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class GuildData {

	private Guild guild;
	private String prefix;
	private Map<User, UserLevel> guildXP;
	
	public GuildData(Guild guild) {
		this.guild=guild;
		guildXP = new HashMap<>();
		if(!loadFromDB())
			save();
	}
	
	
	private void save() {
		guildXP=new HashMap<>();
		
	}
	
	/**
	 * load guild data from DB
	 * 
	 * @return false if guild isn't in DB
	 */
	private boolean loadFromDB() {
		
		prefix = CaexDB.loadPrefixes().get(guild);//TODO re-implement prefix from DB
		guildXP = CaexDB.getLevels().get(guild);//TODO re-implement level map from DB
		
		if(prefix!=null||guildXP!=null)
			return true;
		else
			return false;
		
	}


	public UserLevel getUserLevel(User author) {
		return guildXP.get(author);
	}


	//xp methods
	public void addXP(User author, int XP, TextChannel channel) {

		Logging.debug("Adding "+ XP + "XP to "+ author.getName()+":"+guild.getName());
		
		if(!guildXP.containsKey(author)){
			UserLevel u = new UserLevel(XP);
			guildXP.put(author, u);
			CaexDB.addRow(guild,author,u);
		}
			
				
		if(guildXP.get(author).addXP(XP))
			channel.sendMessage("**Well met __"+author.getAsMention()+"__!** you've advanced to Level: **"+getLevel(author)+"**").queue();
		CaexDB.addXP(guild, author,XP);
	}

	public List<Map.Entry<User, UserLevel>> getGuildRankings() {
		
		return guildXP.entrySet().stream()
				  .sorted(Map.Entry.comparingByValue())
				  .collect(Collectors.toList());
	}

	public int getLevel(User author) {
		return guildXP.get(author).getLevel();
	}
	
	public int getXP(User u){
		return guildXP.get(u).getXP();
	}

	
	//prefix methods
	public String getPrefix() {
		if (prefix==null){
			return CaexConfiguration.getInstance().getPrefix();
		}
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		prefix = prefix.toLowerCase();
		
		this.prefix = prefix;

		CaexDB.savePrefix(guild, prefix);
	}
	
	public void removePrefix() {
		this.prefix=null;
		CaexDB.removePrefix(guild);
	}
	
	
}
