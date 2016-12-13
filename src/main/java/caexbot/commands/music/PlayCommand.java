package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.functions.music.GuildPlayer;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class PlayCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		/*TODO play command process
		 * join voice channel of requested user
		 * start first track in queue for requested guild
		 */
		System.out.println("in play command");
		AudioManager am = event.getGuild().getAudioManager();
		am.openAudioConnection(event.getGuild().getMember(author).getVoiceState().getChannel());
		GuildPlayer gp = GuildPlayerManager.getPlayer(event.getGuild());
		gp.queue("fmI_Ndrxy14");
		while(gp.getQueue().isEmpty()){/*wait*/}
		gp.start();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("play","p");
	}

	@Override
	public String getDescription() {
		return "Start playing the first song in queue";
	}

	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

}
