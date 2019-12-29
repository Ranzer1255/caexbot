package caexbot.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import caexbot.CaexBot;
import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class Logging {

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss Z");

	public static void log(LogLevel level, String message) {
		CaexConfiguration config = CaexConfiguration.getInstance();

		String line = String.format("[%s\t%s] %s\n",level.name(), getTimestamp(), message);

		System.out.print(line);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(config.getLogLocation(), true))) {
			if (level == LogLevel.DEBUG && !config.isDebug()) {
				return;
			}

			writer.write(line);
			writer.close();
		}catch (FileNotFoundException e){
			System.out.println("you shouldn't be going this way");
			config.getLogLocation().getParentFile().mkdirs();
			try {
				config.getLogLocation().createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}catch (Exception ex) {
			System.out.println("Cannot log to file.");
			ex.printStackTrace();
		}
	}

	public static void debug(String message) {
		log(LogLevel.DEBUG, message);
	}

	public static void info(String message) {
		log( LogLevel.INFO, message);
	}

	public static void error(String message) {
		log(LogLevel.ERROR, message);
	}

    public static void log(Exception ex) {
        log(LogLevel.ERROR, ex.getClass().getCanonicalName() + ": " + ex.getMessage());
        for (StackTraceElement trace : ex.getStackTrace()) {
            log(LogLevel.ERROR, "\tat " + trace.toString());
        }
    }
    
    public static void messageBoss(LogLevel level, String message){
    	String line = String.format("[%s\t%s] %s\n",level.name(), getTimestamp(), message);
    	PrivateChannel channel = CaexBot.getJDA().getUserById(CaexConfiguration.getInstance().getOwner()).openPrivateChannel().complete();
    	channel.sendMessage(line).queue();
    }

    private static String getTimestamp() {
        return TIME_FORMAT.format(Calendar.getInstance().getTime());
    }

}
