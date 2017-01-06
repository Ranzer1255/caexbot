package caexbot.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

//		StringBuilder sb = new StringBuilder();
		MessageBuilder mb = new MessageBuilder();
		
		//single command help line
		if(args.length==1){
			EmbedBuilder eb = new EmbedBuilder();
			Logging.debug("help with arg (" +args[0]+")");
			Optional<Describable> opt = getDescribables().stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
			Logging.debug(String.valueOf(opt.isPresent()));
			if(opt.isPresent()){
				Describable d = opt.get();
				eb.setAuthor(d.getName(), null, null);
				eb.setDescription(d.getLongDescription());
				eb.setColor(d.getCatagory().COLOR);
				eb.addField("Usage",d.getUsage(event.getGuild()),false);
				eb.addField("Other Aliases", StringUtil.arrayToString(d.getAlias().subList(1, d.getAlias().size()), ", "), true );
				eb.addField("Catagory", d.getCatagory().toString(), true);
				
				channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();

			}
			
		//full command list	
		}else{
			
			Map<Catagory, List<Describable>> sorted = new HashMap<>();
			
			for (Describable d : getDescribables()) {//TODO catagrize commands via Catagory
				if(sorted.get(d.getCatagory())==null){
					sorted.put(d.getCatagory(), new ArrayList<>());
				}
				sorted.get(d.getCatagory()).add(d);				
			}	
			
			for (Catagory cat : sorted.keySet()) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setAuthor(cat.NAME, null, null);
				eb.setColor(cat.COLOR);
				for (Describable d : sorted.get(cat)) {
					eb.addField(d.getName(), d.getShortDescription(), false);
				}
				mb.setEmbed(eb.build());
				channel.sendMessage(mb.build()).queue();
			}
			
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
