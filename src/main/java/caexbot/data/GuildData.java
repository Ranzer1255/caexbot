package caexbot.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import caexbot.CaexBot;
import caexbot.config.CaexConfiguration;
import caexbot.database.CaexDB;
import caexbot.functions.levels.RoleLevel;
import caexbot.functions.levels.UserLevel;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class GuildData {

	public static final boolean DEFAULT_XP_announceMENT = true;
	private Guild guild;
	
	public GuildData(Guild guild) {
		this.guild=guild;
	}

	//xp methods
	public void addXP(User author, int XP, TextChannel channel) {

		int oldLevel = this.getLevel(author);
		Logging.debug("Adding "+ XP + "XP to "+ author.getName()+":"+guild.getName());
		
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
			   	  "insert into member (guild_id, user_id, xp, last_xp) values (?,?,?,?)"
				+ "on duplicate key update xp=xp+?,last_xp=?;");){
			
			long timestamp = System.currentTimeMillis();
			
			stmt.setString(1, guild.getId());
			stmt.setString(2, author.getId());
			stmt.setInt(3, XP);
			stmt.setLong(4, timestamp);
			stmt.setInt(5, XP);
			stmt.setLong(6, timestamp);
			stmt.executeUpdate();
			
			if(this.getLevel(author)>oldLevel){
				levelUpAlert(author, channel);
			}
			
		} catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}
	
	public void removeXP(User author, int XP, TextChannel channel){
		int oldLevel = this.getLevel(author);
		Logging.debug("Removing "+ XP + "XP from "+ author.getName()+":"+guild.getName());
		
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
			   	  "update member "
			   	  + "set xp = xp-? "
			   	  + "where user_id = ? and guild_id = ?");){
			
			stmt.setString(3, guild.getId());
			stmt.setString(2, author.getId());
			stmt.setInt(1, XP);
			stmt.executeUpdate();
			
			if(this.getLevel(author)<oldLevel){
				levelDownAlert(author, channel);
			}
			
		} catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}

	private void levelUpAlert(User author, TextChannel channel) {
		if(getXPannouncement()){
			channel.sendMessage(String.format("Well met %s!\nYou have advanced to level __**%d**__", author.getAsMention(), getLevel(author))).queue();
		}
		
	}
	private void levelDownAlert(User author, TextChannel channel) {
		if(getXPannouncement()){
			channel.sendMessage(String.format("Oh No %s!\nYou have decreased to level __**%d**__", author.getAsMention(), getLevel(author))).queue();
		}
		
	}

	public void setXPannouncement(boolean announce){
		try(PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
			"update guild set xp_announce=? where guild_id = ?;"	
		)){
			stmt.setBoolean(1, announce);
			stmt.setString(2, guild.getId());
			
			stmt.execute();
		} catch (SQLException e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}
	public boolean getXPannouncement() {
		
		boolean rtn = false;
		try(PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
				"select xp_announce from guild where guild_id = ?"
				)){
			
			stmt.setString(1, guild.getId());
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()){
				rtn = rs.getBoolean(1);
			}
			
		} catch (SQLException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
			rtn = false;
		}
		return rtn;
	}
	public void setJLannouncement(boolean announce){
		try(PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
			"update guild set jl_announce=? where guild_id = ?;"	
		)){
			stmt.setBoolean(1, announce);
			stmt.setString(2, guild.getId());
			
			stmt.execute();
		} catch (SQLException e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}

	public boolean getJLannouncement() {
		
		boolean rtn = false;
		try(PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
				"select jl_announce from guild where guild_id = ?"
				)){
			
			stmt.setString(1, guild.getId());
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()){
				rtn = rs.getBoolean(1);
			}
			
		} catch (SQLException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
			rtn = false;
		}
		return rtn;
	}

	public RoleLevel getRoleLevel(Role role){
		RoleLevel rtn = new RoleLevel(role);
		
		List<Member> members = role.getGuild().getMembersWithRoles(role);
		
		for(Member m:members){
			rtn.addXp(getXP(m.getUser()));
		}
		
		return rtn;
	}
	
	public List<RoleLevel> getRoleRankings(){
		List<RoleLevel> ranking = new ArrayList<>();
		
		for (Role r:guild.getRoles()){
			//TODO filter excluded roles
			ranking.add(new RoleLevel(r));
		}
		
		List<UserLevel> users = getGuildRankings();
		for(UserLevel user: users){
			for(RoleLevel role: ranking){
				if(user.getMember().getRoles().contains(role.ROLE)) role.addXp(user.getXP());
			}
		}
		
		ranking.sort((o1, o2)-> o1.compareTo(o2));
		
		return ranking;
		
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
			
			stmt.close();
		} catch (SQLException e) {
			Logging.error("issue getting GuildRankings");
			Logging.log(e);
		}
		
		return ranking;
	}

	public UserLevel getUserLevel(Member author) {
		
		try {
			UserLevel rtn=null;
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					  "select guild_id, user_id, xp, last_xp from member "
					+ "where user_id = ? and guild_id = ?;"
			);
			stmt.setString(1, author.getUser().getId());
			stmt.setString(2, author.getGuild().getId());
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()){
				rtn = new UserLevel(
						CaexBot.getJDA().getGuildById(rs.getString(1)).getMemberById(rs.getString(2)),
						rs.getInt(3),
						rs.getLong(4)
				);
			}
			stmt.close();
			return rtn;
		} catch (SQLException e) {
			Logging.error("issue trying to get user data");
			Logging.log(e);
			return null;
		}
	}

	public int getLevel(User author) {
		
		return UserLevel.getLevel(getXP(author));
	}
	
	public int getXP(User u){
		
		try (ResultSet rs = CaexDB.getConnection().prepareStatement(
					String.format("select xp from member where guild_id = %s and user_id=%s;",guild.getId(), u.getId())
					).executeQuery()){
			int rtn = -1;
			while (rs.next()){
				rtn = rs.getInt(1);
			}
			
			return rtn;
			
		} catch (SQLException e) {

			Logging.error("issue getting user's XP");
			Logging.log(e);
			return -1;
		}
		
	}

	
	//prefix methods
	public String getPrefix() {
		String prefix=null;
		
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
				"select prefix from guild where guild_id = ?"
		)){
			
			stmt.setString(1, guild.getId());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				prefix=rs.getString(1);
			}
		} catch (SQLException e) {

			Logging.error("issue getting Prefix");
			Logging.log(e);
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
						.prepareStatement(
						"insert into guild (guild_id, prefix) values (?,?) "
						+ "on duplicate key update prefix=?;"
				);
				stmt.setString(1, guild.getId());
				stmt.setString(2, prefix);
				stmt.setString(3, prefix);
				stmt.executeUpdate();
				stmt.close();
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
					.prepareStatement("update guild set prefix = null where guild_id = ?;");
			stmt.setString(1, guild.getId());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {

			Logging.error("issue removeing prefix");
			Logging.log(e);
		}
	}
	public TextChannel getAnnouncementChannel(){
		try (ResultSet rs = CaexDB.getConnection().prepareStatement(
				String.format("SELECT chan_announcement FROM guild where guild_id = %s;",
						guild.getId())).executeQuery()
		){
			while (rs.next()) {
				String chanIdString = rs.getString(1);
				if(chanIdString!=null){
					return guild.getJDA().getTextChannelById(chanIdString);
				}
				return null;
			}
			
		} catch (SQLException e) {
			Logging.error("issue getting announcement channel");
			Logging.log(e);
		}
		return null;
	}

	public void setAnnouncementChannel(TextChannel channel){
		if (channel==null){
			try(PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					String.format("update guild set chan_announcement=null where guild_id='%s';",
							guild.getId()
					))){
				stmt.execute();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}
		
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
				String.format("update guild set chan_announcement='%s' where guild_id='%s';",
						channel.getId(),
						guild.getId())
			)){
			
			stmt.execute();
		} catch (SQLException e) {
			Logging.error("issue setting announcement channel");
			Logging.log(e);
		}
	}

	/**
	 * 
	 * @return admin defined channel for music
	 */
	public TextChannel getDefaultMusicChannel() {
		
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
				"select def_chan_music from guild where guild_id = ?;"	
			);
			
			stmt.setString(1, guild.getId());
			
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			String rtn = rs.getString(1);
			stmt.close();
			try {
				return CaexBot.getJDA().getTextChannelById(rtn);
			} catch (IllegalArgumentException e) {
				return null;
			}
			
			
		} catch (SQLException e) {
			Logging.error("prob getting def_chan_music");
			Logging.log(e);
			return null;
		}
	}
	
	

	/**
	 * used to set the default text channel used for music
	 * @param musicChannel
	 */
	public void setDefaultMusicChannel(TextChannel musicChannel) {
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"update guild set def_chan_music=? where guild_id=?"
			);
			
			stmt.setString(1, musicChannel.getId());
			stmt.setString(2, musicChannel.getGuild().getId());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			Logging.error("problem setting defualt music channel");
			Logging.log(e);
		}
	}


	private void deleteMember(String guild, String user) {
		try {
			PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
					"delete from member where guild_id = ? and user_id = ?"
			);
			
			stmt.setString(1, guild);
			stmt.setString(2, user);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			Logging.error("Problem deleteing member");
			Logging.log(e);
		}
	}

	public ChannelData getChannel(TextChannel channel) {
		return new ChannelData(){

			@Override
			public void setXPPerm(boolean earnEXP) {
				try {
					PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
							  "insert into text_channel (channel_id, guild_id, perm_xp) "
							+ "values (?,?,?) "
							+ "on duplicate key update perm_xp = ?;"
					);
					
					stmt.setString(1, channel.getId());
					stmt.setString(2, channel.getGuild().getId());
					stmt.setBoolean(3, earnEXP);
					stmt.setBoolean(4, earnEXP);
					
					stmt.executeUpdate();
					stmt.close();
					
				} catch (SQLException e) {
					Logging.error("problem setting xp perm");
					Logging.log(e);
				}
				
			}

			@Override
			public boolean getXPPerm() {
				try {
					PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
							"select perm_xp from text_channel "
							+ "where channel_id = ?;"
					);
					
					stmt.setString(1, channel.getId());
					ResultSet rs = stmt.executeQuery();
					
					boolean rtn =ChannelData.DEFAULT_XP_SETTING;
					while (rs.next()) {
						rtn = rs.getBoolean(1);
					}
					stmt.close();
					return rtn;
					
					
				} catch (SQLException e) {
					Logging.error("problem getting XP perm");
					Logging.log(e);
					return true;
				}
			}
			
		};
	}
}
