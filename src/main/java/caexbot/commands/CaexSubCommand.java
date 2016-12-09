package caexbot.commands;

import net.dv8tion.jda.core.entities.Guild;

/**
 * <P>simple class to no-op unnedded methods for subcommands
 * 
 * <P>this class should NOT be used for any commands that <link>HelpCommand</link> will see
 * 
 * @author Ranzer
 *
 */
public abstract class CaexSubCommand extends CaexCommand{

	@Override
	public String getUsage(Guild g) {return null;}// NO-OP this is not needed
}
