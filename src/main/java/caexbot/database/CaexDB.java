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
            	if (!tbl.containsKey(g))
            		tbl.put(g, new HashMap<>());
            	
            	User u = CaexBot.getJDA().getUserById(rs.getString(2));            	
            	UserLevel xp = new UserLevel( rs.getInt(3));
            	Logging.debug(u.getUsername()+": xp"+Integer.toString(xp.getXP())+" Lvl"+Integer.toString(xp.getLevel()));
            	
            	
            	tbl.get(g).put(u, xp);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tbl;
    }

	public static void addRow(Guild guild, User user, UserLevel u) {
		try{
			PreparedStatement stmt = getConnection().prepareStatement("insert into guild_levels (guild_id, user_id, xp) values (?,?,?);");
			stmt.setString(1, guild.getId());
			stmt.setString(2, user.getId());
			stmt.setInt(3, u.getXP());
			stmt.execute();
			
		} catch (Exception e){
			e.printStackTrace();
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
			e.printStackTrace();
		}
		
	}
}
