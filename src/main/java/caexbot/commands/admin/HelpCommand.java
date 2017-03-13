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
			Logging.debug("help with arg (" +args[0]+")");
			Optional<Describable> opt = getDescribables().stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
			Logging.debug(String.valueOf(opt.isPresent()));
			if(opt.isPresent()){
				EmbedBuilder eb = new EmbedBuilder();
				Describable d = opt.get();
				eb.setAuthor(d.getName(), null, null);
				eb.setDescription((d.getLongDescription()!=null)?d.getLongDescription():"long descript wip");
				eb.setColor(d.getCatagory().COLOR);
				eb.addField("Usage",d.getUsage(event.getGuild())!=null?d.getUsage(event.getGuild()):"usage wip",false);
				eb.addField("Other Aliases", (d.getAlias().size()-1)!=0 ? StringUtil.cmdArrayToString(d.getAlias().subList(1, d.getAlias().size()), ", ",event.getGuild()):"*none*", true );
				eb.addField("Catagory", d.getCatagory().toString(), true);
				if (d.getPermissionRequirements() != null) {
					eb.addField("Role Requirement", d.getPermissionRequirements().getName(), true);
				}
				
				channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();

			}
			
		//full command list	
		}else{
			
			Map<Catagory, List<Describable>> catagorized = new HashMap<>();
			
			for (Describable d : getDescribables()) {//TODO sort Categories alphabetically 
				if(catagorized.get(d.getCatagory())==null){
					catagorized.put(d.getCatagory(), new ArrayList<>());
				}
				catagorized.get(d.getCatagory()).add(d);				
			}	
			StringBuilder sb = new StringBuilder();
			EmbedBuilder eb = new EmbedBuilder();
			
			eb.setAuthor("Full Command List", null, null);
			eb.setColor(event.getGuild().getMember(event.getJDA().getSelfUser()).getColor());
			
			for (Catagory cat : catagorized.keySet()) {
				sb.append(String.format("**__%s__**\n", cat.NAME));
				for (Describable d : catagorized.get(cat)) {
					sb.append(String.format("**%s:** %s\n", d.getName(), d.getShortDescription()));
				}
				sb.append("\n");
			}
			eb.setDescription(sb.toString());
			mb.setEmbed(eb.build());
			channel.sendMessage(mb.build()).queue();
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
