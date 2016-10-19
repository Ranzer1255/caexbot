package caexbot.commands;

/**
 * <P>simple class to no-op unnedded methods for subcommands
 * 
 * <P>this class should NOT be used for any commands that <link>HelpCommand</link> will see
 * 
 * @author jrdillingham
 *
 */
public abstract class CaexSubCommand extends CaexCommand{

	@Override
	public String getUsage() {return null;}// NO-OP this is not needed
}
