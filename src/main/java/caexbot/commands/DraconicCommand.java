package caexbot.commands;

import java.util.List;

public interface DraconicCommand{

	List<String> getDraconicAlias();
	CaexCommand getCommand();
}
