package caexbot.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.commands.DraconicCommand;
import caexbot.functions.background.CommandListener;
import caexbot.util.Logging;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand extends CaexCommand implements DraconicCommand, Describable{

	private CommandListener cmds;
	
	public HelpCommand(CommandListener cmds) {
		this.cmds=cmds;
	}
	
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		Logging.debug("Help called");

		StringBuilder sb = new StringBuilder();
		EmbedBuilder eb = new EmbedBuilder();
		
		if(args.length==1){
			Logging.debug("help with arg (" +args[0]+")");
			Optional<Describable> opt = getDescribables().stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
			Logging.debug(String.valueOf(opt.isPresent()));
			if(opt.isPresent()){
				Describable d = opt.get();
				eb.setAuthor(d.getAlias().get(0), null, null);
				eb.setDescription(d.getLongDescription());
				eb.setColor(d.getCatagory().COLOR);
				eb.addField("Usage",d.getUsage(event.getGuild()),false);
				eb.addField("Other Aliases", StringUtil.arrayToString(d.getAlias().subList(1, d.getAlias().size()), ", "), true );
				eb.addField("Catagory", d.getCatagory().toString(), true);
				MessageBuilder mb = new MessageBuilder();
				
				channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
//				
//				sb.append("**Alias:** ")	 .append("[").append(StringUtil.cmdArrayToString(cmd.getAlias(), ", ",event.getGuild())).append("]\n")
//				  .append("**Description** ").append(cmd.getDescription()).append("\n")
//				  .append("**Usage:** ")	 .append(cmd.getUsage(event.getGuild()));
			}
		}else{
			for (Describable d : getDescribables()) {//TODO catagrize commands via Catagory
					sb.append(String.format(
							"**[%s]**: %s\n", 
							StringUtil.cmdArrayToString(d.getAlias(), ", ", event.getGuild()),
							d.getShortDescription()));
				
			}	
			channel.sendMessage(sb.toString()).queue();//TODO change else block into Embeds
		}
	}

	@Override
	public String getUsage(Guild g) {

		return getPrefix(g)+"help [command]";
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("help", "h");
	}

	@Override
	public String getShortDescription() {
		return "Gives a list of avaliable command";
	}

	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("letoclo");
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.ADMIN;
	}
	
	@Override
	public String getLongDescription() {
		// TODO make getLongDescription
		return getShortDescription();
	}

	private List<Describable> getDescribables(){
		List<Describable> rtn = new ArrayList<>();
			
		for (CaexCommand cmd : cmds.getCommands()) {
			if(cmd instanceof Describable){
				rtn.add((Describable) cmd);
			}
		}
		
		return rtn;
	}
}
