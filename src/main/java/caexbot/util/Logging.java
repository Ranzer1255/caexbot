package caexbot.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import caexbot.config.CaexConfiguration;

public class Logging {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss Z");

    public static void log(LogLevel level, String message) {
//      CaexConfiguration config = CaexConfiguration.getInstance();

      String line = String.format("[Unknown][%s\t%s] %s\n",level.name(), getTimestamp(), message);

      System.out.print(line);

//      try (BufferedWriter writer = new BufferedWriter(new FileWriter(config.getLogLocation(), true))) {
//          if (level == LogLevel.DEBUG && !config.isDebug()) {
//              return;
//          }
//
//          writer.write(line);
//      } catch (Exception ex) {
//          System.out.println("Cannot log to file.");
//          ex.printStackTrace();
//      }
  }
    public static void log(Object caller, LogLevel level, String message) {
//        CaexConfiguration config = CaexConfiguration.getInstance();

        String line = String.format("[%s][%s\t%s] %s\n", caller.getClass().getName(),level.name(), getTimestamp(), message);

        System.out.print(line);

//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(config.getLogLocation(), true))) {
//            if (level == LogLevel.DEBUG && !config.isDebug()) {
//                return;
//            }
//
//            writer.write(line);
//        } catch (Exception ex) {
//            System.out.println("Cannot log to file.");
//            ex.printStackTrace();
//        }
    }

    public static void debug(Object caller, String message) {
        log(caller, LogLevel.DEBUG, message);
    }

    public static void info(Object caller, String message) {
        log(caller, LogLevel.INFO, message);
    }

    public static void error(Object caller, String message) {
        log(caller, LogLevel.ERROR, message);
    }

    public static void log(Exception ex) {
        log(LogLevel.ERROR, ex.getClass().getCanonicalName() + ": " + ex.getMessage());
        for (StackTraceElement trace : ex.getStackTrace()) {
            log(LogLevel.ERROR, "\tat " + trace.toString());
        }
    }

    private static String getTimestamp() {
        return TIME_FORMAT.format(Calendar.getInstance().getTime());
    }

}
