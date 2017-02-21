package caexbot.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	/*
	 * this is in guild data because it will eventually be a setting that can be set by a guild admin
	 */
	
	public GuildData(Guild guild) {
		this.guild=guild;
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
		String prefix=null;
		
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement("select prefix from guild where guild_id = ?");
			stmt.setString(1, guild.getId());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				prefix=rs.getString(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (prefix==null){
			return CaexConfiguration.getInstance().getPrefix();
		}
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		if (prefix!=null) {
			prefix = prefix.toLowerCase();
			try {
				PreparedStatement stmt = CaexDB.getConnection()
						.prepareStatement("insert into guild_prefix values (?,?) on duplicate key update prefix=?;");
				stmt.setString(1, guild.getId());
				stmt.setString(2, prefix);
				stmt.setString(3, prefix);
				stmt.execute();
			} catch (Exception e) {
				Logging.error(e.getMessage());
				Logging.log(e);
			} 
		} else {
			removePrefix();
		}
	}
	
	public void removePrefix() {
		
		try {
			PreparedStatement stmt = CaexDB.getConnection()
					.prepareStatement("delete from guild where guild_id = ?;");
			stmt.setString(1, guild.getId());
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CaexDB.removePrefix(guild);
	}
	/**
	 * 
	 * @return last used channel for music
	 */
	public TextChannel getMusicChannel() {
		return null;//TODO pull from DB
	}
	
	

	/**
	 * used to set the last channel used for music
	 * @param musicChannel
	 */
	public void setMusicChannel(TextChannel musicChannel) {
		//TODO push to DB
	}
	
	
}
