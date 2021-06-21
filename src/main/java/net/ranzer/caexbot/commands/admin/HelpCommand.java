package net.ranzer.caexbot.commands.admin;

import java.util.*;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Category;
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
			Optional<Describable> opt = getDescribable(cmds.getCommands()).stream().filter(cc -> cc.getAlias().contains(args[0].toLowerCase())).findFirst();
			if(opt.isPresent()){
				Describable d = opt.get();
				event.getChannel().sendMessage(mb.setEmbeds(getDescription(d,event.getGuild())).build()).queue();
			}
		}else if(args.length == 2){
			Optional<Describable> opt = getDescribable(cmds.getCommands()).stream().filter(cc -> cc.getAlias().contains(args[0].toLowerCase())).findFirst();
			if(opt.isPresent()&&opt.get().hasSubcommands()){
				Describable baseCommand = opt.get();
				Optional<Describable> subOpt = getDescribable(baseCommand.getSubcommands()).stream().filter(cc -> cc.getAlias().contains(args[1].toLowerCase())).findFirst();
				subOpt.ifPresent(describable -> event.getChannel().sendMessage(new MessageBuilder().setEmbeds(getDescription(describable, event.getGuild())).build()).queue());
			}
		//full command list	
		}else{
			
			Map<Category, List<Describable>> categorized = new HashMap<>();
			
			for (Describable d : getDescribable(cmds.getCommands())) {
				categorized.computeIfAbsent(d.getCategory(), k -> new ArrayList<>());
				categorized.get(d.getCategory()).add(d);
			}	
			StringBuilder sb = new StringBuilder();
			EmbedBuilder eb = new EmbedBuilder();
			
			eb.setAuthor("Full Command List", null, null);
			if (event.isFromGuild()) {
				eb.setColor(Objects.requireNonNull(event.getMember()).getColor());
			}
			categorized.keySet().stream().sorted((o1,o2)->
					o1.NAME.compareToIgnoreCase(o2.name())				
			).forEachOrdered(cat -> {
				sb.append(String.format("**__%s__**\n", cat.NAME));
				categorized.get(cat).stream().sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName())
				).forEachOrdered(d -> sb.append(String.format("**%s:** %s\n", d.getName(), d.getShortDescription())));
			sb.append("\n");
			});
			eb.setDescription(sb.toString());
			mb.setEmbeds(eb.build());

			event.getChannel().sendMessage(mb.build()).queue();
		}
	}

	public static MessageEmbed getDescription(Describable d, Guild g) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(d.getName(), null, null);
		eb.setDescription((d.getLongDescription()!=null)?d.getLongDescription():"long description wip");
		eb.setColor(d.getCategory().COLOR);
		eb.addField("Usage",d.getUsage(g)!=null?d.getUsage(g):"usage wip",false);
		eb.addField("Other Aliases",
				(d.getAlias().size()-1)!=0 ? 
						"`"+StringUtil.arrayToString(d.getAlias().subList(1, d.getAlias().size()), "`, `")+"`":
						"*none*",
				true );
		eb.addField("Category", d.getCategory().toString(), true);
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
		return "Gives a list of available command";
	}

	@Override
	public List<String> getDraconicAlias() {
		return Collections.singletonList("letoclo");
	}

	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}
	
	@Override
	public String getLongDescription() {
		
		return getShortDescription()+"\n\n"
				+ "when given a command as an argument, `help` will generate a help page for that command and return it\n\n"
				+ "__**Usage syntax**__\n"
				+ "arguments in `<>` are to be substituted with the requested value\n"
				+ "arguments in `[]` are optional and can be ignored\n"
				+ "arguments in `{}` mean pick one from the list separated by `|`";
	}

	private List<Describable> getDescribable(List<CaexCommand> list){
		List<Describable> rtn = new ArrayList<>();
			
		for (CaexCommand cmd :list) {
			if(cmd instanceof Describable){
				rtn.add((Describable) cmd);
			}
		}
		
		return rtn;
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}
}
