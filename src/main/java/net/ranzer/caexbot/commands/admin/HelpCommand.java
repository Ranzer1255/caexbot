package net.ranzer.caexbot.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Catagory;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.ranzer.caexbot.functions.listeners.CommandListener;
import net.ranzer.caexbot.util.Logging;
import net.ranzer.caexbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends CaexCommand implements DraconicCommand, Describable{

	
	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		Logging.debug("Help called");
		CommandListener cmds = CommandListener.getInstance();

//		StringBuilder sb = new StringBuilder();
		MessageBuilder mb = new MessageBuilder();
		
		//single command help line
		if(args.length==1){
			Optional<Describable> opt = getDescribables(cmds.getCommands()).stream().filter(cc -> cc.getAlias().contains(args[0].toLowerCase())).findFirst();
			if(opt.isPresent()){
				Describable d = opt.get();
				event.getChannel().sendMessage(mb.setEmbed(getDescription(d,event.getGuild())).build()).queue();
			}
		}else if(args.length == 2){
			Optional<Describable> opt = getDescribables(cmds.getCommands()).stream().filter(cc -> cc.getAlias().contains(args[0].toLowerCase())).findFirst();
			if(opt.isPresent()&&opt.get().hasSubcommands()){
				Describable baseCommand = opt.get();
				Optional<Describable> subOpt = getDescribables(baseCommand.getSubcommands()).stream().filter(cc -> cc.getAlias().contains(args[1].toLowerCase())).findFirst();
				if(subOpt.isPresent()){
					event.getChannel().sendMessage(new MessageBuilder().setEmbed(getDescription(subOpt.get(),event.getGuild())).build()).queue();
				}
			}
		//full command list	
		}else{
			
			Map<Catagory, List<Describable>> catagorized = new HashMap<>();
			
			for (Describable d : getDescribables(cmds.getCommands())) {
				if(catagorized.get(d.getCatagory())==null){
					catagorized.put(d.getCatagory(), new ArrayList<>());
				}
				catagorized.get(d.getCatagory()).add(d);				
			}	
			StringBuilder sb = new StringBuilder();
			EmbedBuilder eb = new EmbedBuilder();
			
			eb.setAuthor("Full Command List", null, null);
			if (event.getGuild() !=null) {
				eb.setColor(event.getGuild().getMember(event.getJDA().getSelfUser()).getColor());
			}
			catagorized.keySet().stream().sorted((o1,o2)->
					o1.NAME.compareToIgnoreCase(o2.name())				
			).forEachOrdered(cat -> {
				sb.append(String.format("**__%s__**\n", cat.NAME));
				catagorized.get(cat).stream().sorted((o1, o2) -> {return o1.getName().compareToIgnoreCase(o2.getName());}
				).forEachOrdered(d -> {sb.append(String.format("**%s:** %s\n", d.getName(), d.getShortDescription()));});
			sb.append("\n");
			});
			eb.setDescription(sb.toString());
			mb.setEmbed(eb.build());

			event.getChannel().sendMessage(mb.build()).queue();
		}
	}

	public static MessageEmbed getDescription(Describable d, Guild g) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(d.getName(), null, null);
		eb.setDescription((d.getLongDescription()!=null)?d.getLongDescription():"long descript wip");
		eb.setColor(d.getCatagory().COLOR);
		eb.addField("Usage",d.getUsage(g)!=null?d.getUsage(g):"usage wip",false);
		eb.addField("Other Aliases",
				(d.getAlias().size()-1)!=0 ? 
						"`"+StringUtil.arrayToString(d.getAlias().subList(1, d.getAlias().size()), "`, `")+"`":
						"*none*",
				true );
		eb.addField("Catagory", d.getCatagory().toString(), true);
		if (d.getPermissionRequirements() != null) {
			eb.addField("Role Requirement", d.getPermissionRequirements().getName(), true);
		}
		
		return eb.build();
	}

	@Override
	public String getUsage(Guild g) {

		return getPrefix(g)+"help [<command>]`";
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
		
		return getShortDescription()+"\n\n"
				+ "when given a command as an agrument, `help` will generate a help page for that command and return it\n\n"
				+ "__**Usage syntax**__\n"
				+ "arguemnts in `<>` are to be substituded with the requested value\n"
				+ "arguments in `[]` are optional and can be ignored\n"
				+ "arguments in `{}` mean pick one from the list seperated by `|`";
	}

	private List<Describable> getDescribables(List<CaexCommand> list){
		List<Describable> rtn = new ArrayList<>();
			
		for (CaexCommand cmd :list) {
			if(cmd instanceof Describable){
				rtn.add((Describable) cmd);
			}
		}
		
		return rtn;
	}

	@Override
	public boolean isAplicableToPM() {
		return true;
	}
}