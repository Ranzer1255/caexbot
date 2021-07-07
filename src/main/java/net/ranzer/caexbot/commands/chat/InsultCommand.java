package net.ranzer.caexbot.commands.chat;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.util.Logging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ranzer
 */
public class InsultCommand extends BotCommand implements Describable, DraconicCommand {
	
	private static final String THIS_BOT_INSULT = "Oh ha ha.... making me insult myself.... How original\n"
			+ "Well, since I'm a good sport I will";
	private static final String SELF_INSULT = "*looks confused* yourself? but.... ok...";
	private static final String OWNER_ONLINE = "You want me to insult him?!?!.... \n I'm sorry but I can't insult *him*.... he'll *__KILL__* me!!";
	private static final String OWNER_OFFLINE = "*looks around nervously* ... he's not here right now....\n"
			+ "alright, I'll do it..... **but** I wont tag him! *gulp*\n"
			+ "if he says \"sleep\", its on your head!";
	@SuppressWarnings("SpellCheckingInspection")
	private static final String OWNER_IDLE = "*looks around nervously* ... he's not here righ.... wait!\n"
			+ "I see him.... He's only Idle, might be back any second, especially with that ping you just did\n"
			+ "best not risk it, sorry!";
	private static final String OWNER_INSULT = "*sputters* B...B..Boss? I would never dream.... *chuckles nervously* If.. If you insist....";
	private static final String OTHER_BOT_INSULT =
		      "```\n"
		    + "01111001011011110111010100100111011100100110010100100000 \n"
			+ "01110011011011110010000001110011011101000111010101110000 \n"
			+ "01101001011001000010000001111001011011110111010100100000 \n"
			+ "01100011011000010110111000100000011100100110010101100001 \n"
			+ "01100100001000000110001101100001011100000111010001100011 \n"
			+ "01101000011000010111001100100001001000000110001001100101 \n"
			+ "01100101011100000010000001100010011011110110111101110000 \n"
			+ "```";
	
	private static final List<String> insults = loadInsults();
	public static User lastOwnerInsult = null;
	
	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();
		
		for ( Member m : event.getMessage().getMentionedMembers()) {
			
			//owner insulted
			if(m.getUser().getId().equals(CaexConfiguration.getInstance().getOwner())){
				//owner insulted self
				if(Objects.equals(event.getMember(), m)){
					event.getChannel().sendMessage(OWNER_INSULT).queue();
					sb.append(m.getAsMention()).append(", ");
					continue;
				}
				
				//owner status check
				switch (m.getGuild().retrieveMemberById(m.getId()).complete().getOnlineStatus()){
				case ONLINE:
					event.getChannel().sendMessage(OWNER_ONLINE).queue();
					break;
				
				case INVISIBLE:
				case DO_NOT_DISTURB:
				case OFFLINE:
					event.getChannel().sendMessage(OWNER_OFFLINE).queue();
					lastOwnerInsult=event.getAuthor();
					sb.append(m.getEffectiveName()).append(", ");
					break;
				case IDLE:
					event.getChannel().sendMessage(OWNER_IDLE).queue();
					break;
				default :
					event.getChannel().sendMessage("That's odd.... He's got a weird status, best not chance it sorry.").queue();
				}
				continue;
			}
			
			//user insulted themselves
			if(Objects.equals(event.getMember(), m)){
				event.getChannel().sendMessage(SELF_INSULT).queue();
			}
			
			//user insulted this bot
			if(m.equals(event.getGuild().getSelfMember())){
				event.getChannel().sendMessage(THIS_BOT_INSULT).queue();
			}
			
			if(m.getUser().isBot()){
				event.getChannel().sendMessage(m.getAsMention()+"\n"+OTHER_BOT_INSULT).queue();
			} else {				
				sb.append(m.getAsMention()).append(", ");
			}
		}
		if(sb.length()==0) return;//don't throw an insult if no one was tagged.
	
		String insult = bardicInsult();
		
		sb.append(insult);
		event.getChannel().sendMessage(sb.toString()).queue();
	}
	
	private String bardicInsult(){
		return insults.get(ThreadLocalRandom.current().nextInt(insults.size()));
	}
	
	@Override
	public List<String> getAlias() {
		return Arrays.asList("insult","offend","curse*");
	}

	@Override
	public List<String> getDraconicAlias() {
		return Collections.singletonList("chikohk");
	}

	@Override
	public Category getCategory() {
		return Category.CHAT;
	}

	@Override
	public String getShortDescription() {
		return "Give them a slap in the face!";
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" <Tagged users you want to insult>`";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n"
				+ "Tag the people you want to insult. can insult more than one at a time.";
	}

	@Override
	public boolean isApplicableToPM() {
		return false;
	}
	
	private static List<String> loadInsults() {

		List<String> rtn = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(InsultCommand.class.getResourceAsStream("/DnDInsults.txt"))));
			String line;
			while ((line = br.readLine())!= null) {
				rtn.add(line);
				Logging.debug("read in insult: "+line);
			}
			br.close();
		} catch ( Exception e ) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
		return rtn;
		
	}

}
