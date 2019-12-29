package net.ranzer.caexbot.data.mysql;

import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.data.GuildData;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.database.CaexDB;
import net.ranzer.caexbot.util.Logging;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * container for all the guild data objects
 * may have a conflict with JDA GuildManager... we'll see
 * 
 * @author Ranzer
 *
 */
public class MySQLGuildManager extends GuildManager {
	
	/*
	 * update the DB to match things that happend while Caex was offline
	 */
	public MySQLGuildManager(){
		Logging.info("updating DB to things that happend while offline");
		
		Logging.info("adding new guilds");
		addNewGuilds();
		
		Logging.info("removeing old guilds");
		removeOldGuilds();
		
		Logging.info("updating members");
		updateMembers();
		
		
	}

	public GuildData getGuildData(Guild key){
		return new MySQLGuildData(key);
	}

	//convenance passthrough methods
	public String getPrefix(Guild key){
		return new MySQLGuildData(key).getPrefix();
	}

	public void setPrefix(Guild key, String prefix){
		new MySQLGuildData(key).setPrefix(prefix);
	}

	public void removePrefix(Guild key){
		new MySQLGuildData(key).removePrefix();
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		
		try(PreparedStatement stmt = CaexDB.getConnection().prepareStatement("insert into guild(guild_id) values (?)")){
			stmt.setString(1, event.getGuild().getId());
			stmt.execute();
		} catch (SQLException e) {
			Logging.error("issue joining guild to DB");
			Logging.log(e);
		}
		
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"delete from guild where guild_id = ?"
			)){
			
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
		
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"delete from member where guild_id=? and user_id = ?"
			)){
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

	private void addNewGuilds() {
		List<Guild> dbGuilds = pullGuildsFromDB();
		
		for(Guild g:CaexBot.getJDA().getGuilds()){
			if(!dbGuilds.contains(g)){
				try 
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
	}
	private List<Guild> pullGuildsFromDB() {
		List<Guild> rtn = new ArrayList<>();
		
		try (ResultSet rs = CaexDB.getConnection().prepareStatement(
				"select guild_id from guild" 
		).executeQuery()){
			
			while(rs.next()){
				rtn.add(CaexBot.getJDA().getGuildById(rs.getString(1)));
			}
			
		} catch (SQLException e) {
			Logging.error("issue pulling data from DB");
			Logging.log(e);
		}
		
		return rtn;
	}

	private void removeOldGuilds() {
		try (ResultSet rs = CaexDB.getConnection().prepareStatement(
					"select guild_id from guild" ,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE
			).executeQuery()){

			while(rs.next()){
				if(CaexBot.getJDA().getGuildById(rs.getString(1))==null)
					rs.deleteRow();
			}
			
		} catch (SQLException e) {
			Logging.error("problem removing old guilds from DB");
			Logging.log(e);
		}
	}

	private void updateMembers() {
		try (ResultSet rs = CaexDB.getConnection().prepareStatement(
				"select guild_id, user_id from member" ,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE
			).executeQuery()){
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


