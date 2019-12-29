package caexbot.data.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import caexbot.CaexBot;
import caexbot.config.CaexConfiguration;
import caexbot.data.ChannelData;
import caexbot.data.GuildData;
import caexbot.database.CaexDB;
import caexbot.functions.levels.RoleLevel;
import caexbot.functions.levels.UserLevel;
import caexbot.util.Logging;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class MySQLGuildData extends GuildData {

	public static final boolean DEFAULT_XP_announceMENT = true;
	private Guild guild;
	
	public MySQLGuildData(Guild guild) {
		this.guild=guild;
	}

	//xp methods
	@Override
	public void addXP(User author, int XP, MessageChannel channel) {

		int oldLevel = this.getLevel(author);
		Logging.debug("Adding "+ XP + "XP to "+ author.getName()+":"+guild.getName());
		
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
			   	  "insert into member (guild_id, user_id, xp, last_xp) values (?,?,?,?)"
				+ "on duplicate key update xp=xp+?,last_xp=?;")){
			
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
	
	@Override
	public void removeXP(User author, int XP, MessageChannel channel){
		int oldLevel = this.getLevel(author);
		Logging.debug("Removing "+ XP + "XP from "+ author.getName()+":"+guild.getName());
		
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
			   	  "update member "
			   	  + "set xp = xp-? "
			   	  + "where user_id = ? and guild_id = ?")){
			
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

	private void levelUpAlert(User author, MessageChannel channel) {
		if(getXPAnnouncement()){
			channel.sendMessage(String.format("Well met %s!\nYou have advanced to level __**%d**__", author.getAsMention(), getLevel(author))).queue();
		}
		
	}
	private void levelDownAlert(User author, MessageChannel channel) {
		if(getXPAnnouncement()){
			channel.sendMessage(String.format("Oh No %s!\nYou have decreased to level __**%d**__", author.getAsMention(), getLevel(author))).queue();
		}
		
	}

	@Override
	public void setXPAnnouncement(boolean announce){
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
	@Override
	public boolean getXPAnnouncement() {
		
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
	@Override
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

	@Override
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

	@Override
	public RoleLevel getRoleLevel(Role role){
		RoleLevel rtn = new RoleLevel(role);
		
		List<Member> members = role.getGuild().getMembersWithRoles(role);
		
		for(Member m:members){
			rtn.addXp(getXP(m.getUser()));
		}
		
		return rtn;
	}
	
	@Override
	public List<RoleLevel> getRoleRankings(){
		List<RoleLevel> ranking = new ArrayList<>();
		
		for (Role r:guild.getRoles()){
			if(getRoleExcluded(r))continue;
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

	private boolean getRoleExcluded(Role r) {
		try(ResultSet rs = CaexDB.getConnection().prepareStatement(
			String.format("select xp_excluded from role where role_id = %s and guild_id = %s",
				r.getId(),
				guild.getId()
			)
		).executeQuery()){
			while(rs.next()){
				return rs.getBoolean(1);
			}
		} catch (SQLException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		return false;
	}
	
	@Override
	public void setExcludedRole(Role r, boolean exclude){
		try (PreparedStatement stmt = CaexDB.getConnection().prepareStatement(
				"insert into role (guild_id, role_id, xp_excluded) "
				+ "values (?,?,?) "
				+ "on duplicate key update"
				+ "xp_excluded = ?")){
			
			stmt.setString(1, guild.getId());
			stmt.setString(2, r.getId());
			stmt.setBoolean(3, exclude);
			stmt.setBoolean(4, exclude);
			
		} catch (SQLException e) {
			Logging.error("issue setting excluded roles");
			Logging.log(e);
		}
	}

	@Override
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

	@Override
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

	@Override
	public int getLevel(User author) {
		
		return UserLevel.getLevel(getXP(author));
	}
	
	@Override
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
	@Override
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
	
	@Override
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
	
	@Override
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
	@Override
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

	@Override
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
	@Override
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
	@Override
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

	@Override
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
