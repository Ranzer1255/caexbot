package caexbot.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import caexbot.CaexBot;
import caexbot.database.CaexDB;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * container for all the guild data objects
 * may have a conflict with JDA GuildManager... we'll see
 * 
 * @author Ranzer
 *
 */
public class GuildManager extends ListenerAdapter{
	
	/*
	 * update the DB to match things that happend while Caex was offline
	 */
	static{
		Logging.info("updating DB to things that happend while offline");
		
		Logging.info("adding new guilds");
		addNewGuilds();
		
		Logging.info("removeing old guilds");
		removeOldGuilds();
		
		Logging.info("updating members");
		updateMembers();
		
		
	}

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
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		
		try(PreparedStatement stmt = CaexDB.getConnection().prepareStatement("insert into guild(guild_id) values (?)")){
			stmt.setString(1, event.getGuild().getId());
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		super.onGuildLeave(event);
		
		try{//TODO change to Try with Resouces
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"delete from guild where guild_id = ?"
			);
			
			stmt.setString(1, event.getGuild().getId());
			Logging.info(String.format("Delteting guild %s(%s",
					event.getGuild().getName(),
					event.getGuild().getId())
			);
			
			Logging.debug(String.format("%d rows updated", stmt.executeUpdate()));
			
		} catch (SQLException e){
			Logging.error("issue removiing guild from DB: " +event.getGuild().getName());
			Logging.log(e);
		}
	}
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		super.onGuildMemberLeave(event);
		
		try {//TODO change to Try with Resouces
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"delete from member where guild_id=? and user_id = ?"
			);
			
			stmt.setString(1, event.getGuild().getId());
			stmt.setString(2, event.getMember().getUser().getId());
			Logging.info(String.format("Removing user %s(%s) from guild %s(%s)",
					event.getMember().getEffectiveName(),
					event.getMember().getUser().getId(), 
					event.getGuild().getName(),
					event.getGuild().getId()));
			Logging.debug(String.format("%d rows updated", stmt.executeUpdate()));
		} catch (SQLException e) {
			Logging.error("issue removing user from the DB");
			Logging.log(e);
		}
		
		
	}

	private static void addNewGuilds() {
		List<Guild> guilds = CaexBot.getJDA().getGuilds();
		for(Guild g:guilds){
			try 
				
				//TODO this hits the DB with a qurery for every guild on startup.... may optimize this later
				(PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
						"insert into guild(guild_id) values (?) on duplicate key update guild_id = guild_id"
				)){
				stmt.setString(1, g.getId());
				
				Logging.info(String.format("%d rows added to Guild Table", stmt.executeUpdate()));
				
				
			} catch (SQLException e) {
				Logging.error("issue adding new guilds to the DB");
				Logging.log(e);
			}
		}
	}

	private static void removeOldGuilds() {
		try {//TODO change to Try with Resouces
			ResultSet rs = CaexDB.getConnection().prepareStatement(
					"select guild_id from guild" ,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE
			).executeQuery();
			
			while(rs.next()){
				if(CaexBot.getJDA().getGuildById(rs.getString(1))==null)
					rs.deleteRow();
			}
			
		} catch (SQLException e) {
			Logging.error("problem removing old guilds from DB");
			Logging.log(e);
		}
	}

	private static void updateMembers() {
		try {//TODO change to Try with Resouces
			ResultSet rs = CaexDB.getConnection().prepareStatement(
				"select guild_id, user_id from member" ,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE
			).executeQuery();
			
			while (rs.next()){
				if (CaexBot.getJDA().getGuildById(rs.getString(1)).getMemberById(rs.getString(2))==null){
					rs.deleteRow();
				}
			}
		} catch (SQLException e) {
			Logging.error("issue updating members");
			Logging.log(e);
		}
		
	}
	
}


