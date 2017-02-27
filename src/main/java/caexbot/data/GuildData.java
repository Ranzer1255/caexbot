package caexbot.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import caexbot.CaexBot;
import caexbot.config.CaexConfiguration;
import caexbot.database.CaexDB;
import caexbot.functions.levels.UserLevel;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class GuildData {

	private Guild guild;
	
	public GuildData(Guild guild) {
		this.guild=guild;
	}


	//xp methods
	public void addXP(User author, int XP, TextChannel channel) {

		Logging.debug("Adding "+ XP + "XP to "+ author.getName()+":"+guild.getName());
		
		try{
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement("insert into member (guild_id, user_id, xp) values (?,?,?)"
																		   + "on duplicate key update xp=xp+?;");
			stmt.setString(1, guild.getId());
			stmt.setString(2, author.getId());
			stmt.setInt(3, XP);
			stmt.setInt(4, XP);
			stmt.execute();
			
		} catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}

	public List<UserLevel> getGuildRankings() {
		
		List<UserLevel> ranking = new ArrayList<>(); 
		
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"select guild_id, user_id, xp from member where guild_id=? order by xp desc;"
			);
			
			stmt.setString(1, guild.getId());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				Member member = CaexBot.getJDA().getGuildById(rs.getString(1)).getMemberById(rs.getString(2));
				if(member==null){
					deleteMember(rs.getString(1),rs.getString(2));
					continue;
				}
				ranking.add(new UserLevel(member, rs.getInt(3)));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ranking;
	}

	public int getLevel(User author) {
		
		return UserLevel.getLevel(getXP(author));
	}
	
	public int getXP(User u){
		
		try {
			ResultSet rs = CaexDB.getConnection().prepareStatement(
					String.format("select xp from member where guild_id = %s and user_id=%s;",guild.getId(), u.getId())
					).executeQuery();
			rs.next();
			return rs.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
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


	private void deleteMember(String guild, String user) {
		try {
			CaexDB.getConnection().prepareStatement(
				String.format("delete from member where guild_id = %s and user_id = %s",
						guild, user
			)).execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
