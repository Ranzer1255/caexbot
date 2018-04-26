package caexbot.commands.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.commands.DraconicCommand;
import caexbot.config.CaexConfiguration;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Ranzer
 */
public class InsultCommand extends CaexCommand implements Describable, DraconicCommand {
	
	private static final String THIS_BOT_INSULT = "Oh ha ha.... making me insult myself.... How original\n"
			+ "Well, sence I'm a good sport I will";
	private static final String SELF_INSULT = "*looks confused* yourself? but.... ok...";
	private static final String OWNER_ONLINE = "You want me to insult him?!?!.... \n I'm sorry but I can't insult *him*.... he'll *__KILL__* me!!";
	private static final String OWNER_OFFLINE = "*looks around nervously* ... he's not here right now....\n"
			+ "alright, I'll do it..... **but** I wont tag him! *gulp*\n"
			+ "if he says \"sleep\", its on your head!";
	private static final String OWNER_IDLE = "*looks around nervously* ... he's not here righ.... wait!\n"
			+ "I see him.... He's only Idle, might be back any second, especialy with that ping you just did\n"
			+ "best not risk it, sorry!";
	private static final String OWNER_INSULT = "*sputters* B...B..Boss? I would never dream.... *chuckles nervously* If.. If you insist....";
	private static final String OTHER_BOT_INSULT =
		      "```\n"
		    + "01111001 01101111 01110101 00100111 01110010 01100101 00100000 \n"
			+ "01110011 01101111 00100000 01110011 01110100 01110101 01110000 \n"
			+ "01101001 01100100 00100000 01111001 01101111 01110101 00100000 \n"
			+ "01100011 01100001 01101110 00100000 01110010 01100101 01100001 \n"
			+ "01100100 00100000 01100011 01100001 01110000 01110100 01100011 \n"
			+ "01101000 01100001 01110011 00100001 00100000 01100010 01100101 \n"
			+ "01100101 01110000 00100000 01100010 01101111 01101111 01110000 \n"
			+ "```";
	
	private static List<String> insults = loadInsults();
	public static User lastOwnerInsult = null;
	
	@Override
	public void process(String[] args,MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();
		
		for ( Member m : event.getMessage().getMentionedMembers()) {
			
			//owner insulted
			if(m.getUser().getId().equals(CaexConfiguration.getInstance().getOwner())){
				//owner insulted self
				if(event.getMember().equals(m)){
					event.getChannel().sendMessage(OWNER_INSULT).queue();
					sb.append(m.getAsMention()+", ");
					continue;
				}
				
				//owner status check
				switch (m.getOnlineStatus()){
				case ONLINE:
					event.getChannel().sendMessage(OWNER_ONLINE).queue();
					break;
				
				case INVISIBLE:
				case DO_NOT_DISTURB:
				case OFFLINE:
					event.getChannel().sendMessage(OWNER_OFFLINE).queue();
					lastOwnerInsult=event.getAuthor();
					sb.append(m.getEffectiveName()+", ");
					break;
				case IDLE:
					event.getChannel().sendMessage(OWNER_IDLE).queue();
					break;
				default :
					event.getChannel().sendMessage("Thats odd.... He's got a weird status, best not chance it sorry.").queue();
				}
				continue;
			}
			
			//user insulted themselves
			if(event.getMember().equals(m)){
				event.getChannel().sendMessage(SELF_INSULT).queue();
			}
			
			//user insulted this bot
			if(m.equals(event.getGuild().getSelfMember())){
				event.getChannel().sendMessage(THIS_BOT_INSULT).queue();
			}
			
			if(m.getUser().isBot()){
				event.getChannel().sendMessage(m.getAsMention()+"\n"+OTHER_BOT_INSULT).queue();
			} else {				
				sb.append(m.getAsMention()+", ");
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
		return Arrays.asList("chikohk");
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

	@Override
	public String getShortDescription() {
		return "Give them a slap in the face!";
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" <Taged users you want to insult>`";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n"
				+ "Tag the people you want to insult. can insult more than one at a time.\n\n"
				+ "Powered by [pangloss.com](http://www.pangloss.com/seidel/Shaker/index.html?)";
	}

	@Override
	public boolean isAplicableToPM() {
		return false;
	}
	
	private static List<String> loadInsults() {

		List<String> rtn = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(InsultCommand.class.getResourceAsStream("/DnDInsults.txt")));
			String line=null;
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
