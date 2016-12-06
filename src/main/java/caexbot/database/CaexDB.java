package caexbot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import caexbot.CaexBot;
import caexbot.config.CaexConfiguration;
import caexbot.functions.levels.UserLevel;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;


public class CaexDB {

	private static Connection connection;
	
	private static Connection getConnection(){
		CaexConfiguration config = CaexConfiguration.getInstance();
		try {
			if (connection == null || connection.isClosed()){
				String DBMS = config.getDatabaseManagementSystem();
				String host = config.getDatabaseHostname();
				Integer port = config.getDatabasePort();
				String DB = config.getDatabaseName();
				String user = config.getDatabaseUsername();
				String pw = config.getDatabasePassword();
				 
				connection = DriverManager.getConnection(String.format("jdbc:%s://%s:%d/%s?useSSL=false",DBMS,host,port,DB), user, pw);
			}
			
			return connection;
		} catch (SQLException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
			return null;
		}
	}

	public static Map<Guild, Map<User,UserLevel>> getLevels() {
		Map<Guild, Map<User,UserLevel>> tbl = new HashMap<>();

		try {
			PreparedStatement stmt = getConnection().prepareStatement("select * from guild_levels");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Guild g = CaexBot.getJDA().getGuildById(rs.getString(1));
				if (g==null){
					deleteGuild(rs.getString(1));
					continue;
				}
				if (!tbl.containsKey(g))
					tbl.put(g, new HashMap<>());

				User u = CaexBot.getJDA().getUserById(rs.getString(2));
				if (u==null){
					deleteUser(rs.getString(2));
					continue;
				}
				UserLevel xp = new UserLevel( rs.getInt(3));
				Logging.debug(u.getName()+": xp"+Integer.toString(xp.getXP())+" Lvl"+Integer.toString(xp.getLevel()));


				tbl.get(g).put(u, xp);
			}

		} catch (Exception ex) {
			Logging.error(ex.getMessage());
			Logging.log(ex);
		}

		return tbl;
	}

	private static void deleteGuild(String g) {
		Logging.info(String.format("Guild %s no longer exists, removing entries from DB", g));
		try{
			PreparedStatement stmt = getConnection().prepareStatement("delete from guild_levels where guild_id = ?");
			stmt.setString(1, g);
			stmt.execute();
		} catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}

	private static void deleteUser(String u) {
		Logging.info(String.format("User_ID %s no longer exists, removing entries from DB", u));
		try{
			PreparedStatement stmt = getConnection().prepareStatement("delete from guild_levels where user_id = ?");
			stmt.setString(1, u);
			stmt.execute();
		} catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}

	public static void addRow(Guild guild, User user, UserLevel u) {
		try{
			PreparedStatement stmt = getConnection().prepareStatement("insert into guild_levels (guild_id, user_id, xp) values (?,?,?);");
			stmt.setString(1, guild.getId());
			stmt.setString(2, user.getId());
			stmt.setInt(3, u.getXP());
			stmt.execute();
			
		} catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
	}

	public static void addXP(Guild guild, User user, int XP) {
		try{
			PreparedStatement stmt = getConnection().prepareStatement("update guild_levels set xp = (xp + ?) where guild_id = ? and user_id = ?;");
			stmt.setInt(1, XP);
			stmt.setString(2, guild.getId());
			stmt.setString(3, user.getId());
			stmt.execute();
		}catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
	}

	public static void savePrefix(Guild key, String prefix) {
		try{
			PreparedStatement stmt = getConnection().prepareStatement("insert into guild_prefix values (?,?) on duplicate key update prefix=?;");
			stmt.setString(1, key.getId());
			stmt.setString(2, prefix);
			stmt.setString(3, prefix);
			stmt.execute();
		}catch (Exception e){
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
	}
	
	public static Map<Guild,String> loadPrefixes(){
		Map<Guild,String> rtn = new HashMap<>();
		
		try {
			PreparedStatement stmt = getConnection().prepareStatement("select * from guild_prefix");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Guild g = CaexBot.getJDA().getGuildById(rs.getString(1));
				String p = rs.getString(2);             	
				rtn.put(g, p);
			}

		} catch (Exception ex) {
			Logging.error(ex.getMessage());
			Logging.log(ex);
		}

		return rtn;
	}

	public static void removePrefix(Guild key) {
		PreparedStatement stmt;
		try {
			stmt = getConnection().prepareStatement("delete from guild_prefix where guild_id = ?;");
			stmt.setString(1, key.getId());
			stmt.execute();
			
		} catch (SQLException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
	}
}
