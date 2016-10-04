package caexbot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import caexbot.CaexBot;
import caexbot.functions.levels.UserLevel;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;


public class CaexDB {

	private static Connection connection;
	
	private static Connection getConnection(){
		try {
			if (connection == null || connection.isClosed()){
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/caexdb?useSSL=false", "Caexbot", "testing");
			}
			
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Map<Pair<Guild,User>, UserLevel> getLevels() {
		Map<Pair<Guild,User>, UserLevel> tbl = new HashMap<>();

        try {
            PreparedStatement stmt = getConnection().prepareStatement("select * from guild_levels");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	Guild g = CaexBot.getJDA().getGuildById(rs.getString(1));
            	User u = CaexBot.getJDA().getUserById(rs.getString(2));
            	Pair<Guild, User> key = new ImmutablePair<>(g, u);
            	
            	UserLevel xp = new UserLevel( rs.getInt(3));
            	tbl.put(key, xp);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tbl;
    }

	public static void addRow(Pair<Guild, User> key, UserLevel u) {
		try{
			PreparedStatement stmt = getConnection().prepareStatement("insert into guild_levels (guild_id, user_id, xp) values (?,?,?);");
			stmt.setString(1, key.getLeft().getId());
			stmt.setString(2, key.getRight().getId());
			stmt.setInt(3, u.getXP());
			stmt.execute();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

	public static void addXP(Pair<Guild, User> key, int XP) {
		try{
			PreparedStatement stmt = getConnection().prepareStatement("update guild_levels set xp = (xp + ?) where guild_id = ? and user_id = ?;");
			stmt.setInt(1, XP);
			stmt.setString(2, key.getLeft().getId());
			stmt.setString(3, key.getRight().getId());
			stmt.execute();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
}
