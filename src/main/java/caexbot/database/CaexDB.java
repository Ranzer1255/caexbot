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
				connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/caexdb", "Caexbot", "testing");
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
}
