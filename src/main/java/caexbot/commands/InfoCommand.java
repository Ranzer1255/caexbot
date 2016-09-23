package caexbot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

import caexbot.references.CaexBotReference;

public class InfoCommand implements CommandExecutor {

    @Command(aliases = "info", description = "Shows some information about the bot.", usage = "!info [author|time]")
    public String onInfoCommand(String[] args) {
        if (args.length > 1) { // more than 1 argument
            return "To many arguments!";
        }
        if (args.length == 0) { // !info
            return "- **Author:** Ranzer\n" +
                   "- **Language:** Java\n" +
                   "- **Command-Lib:** sdcf4j\n" +
                   "- **Github Rebo:** https://github.com/Sgmaniac1255/caexbot";
        }
        if (args.length == 1) { // 1 argument
            if (args[0].equals("author")) { // !info author
                return "- **Name:** Ranzer\n" +
                       "- **Age:** 27\n" +
                       "- **Website:** http://www.github.com/sgmaniac1255";
            }
            if (args[0].equals("time")) { // !info time
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date currentDate = new Date(System.currentTimeMillis());
                return "It's " + format.format(currentDate);
            }
        }
        return "Unknown argument!";
    }

}
