package caexbot.commands;

import java.util.List;

public interface DraconicCommand{

	public List<String> getDraconicAlias();
	public CaexCommand getCommand();
}
