package net.ranzer.caexbot.functions.music;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.functions.music.commands.MusicCommand;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.functions.music.events.*;
import net.ranzer.caexbot.util.Logging;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Handler;

public class MusicListener implements MusicEventListener{
	private final Guild guild;
	private TextChannel lastMusicChannel;
	private Message nowPlayingMessage;

	public MusicListener(Guild g) {
		guild = g;
		g.getJDA().addEventListener(buttonHandler);
	}

	private final ListenerAdapter buttonHandler = new ListenerAdapter() {
		@Override
		public void onButtonClick(@NotNull ButtonClickEvent event) {
			if (notInSameVoiceChannel(event.getUser())) {
				event.reply("you must be in voice with me to use these buttons").setEphemeral(true).queue();
				return;
			}
			switch (event.getComponentId()){
				case "ml_skip":
					Logging.debug("skip clicked");
					GuildPlayerManager.getPlayer(event.getGuild()).playNext(true);
					break;
				case "ml_stop":
					Logging.debug("stop clicked");
					GuildPlayerManager.getPlayer(event.getGuild()).stop(true);
					//these steps will be moved to a "stop event"
					//TODO make a "stop" event
					clearButtons(nowPlayingMessage);
					nowPlayingMessage=null;
					break;
				case "ml_pause":
					Logging.debug("pause clicked");
					GuildPlayerManager.getPlayer(event.getGuild()).pause();
			}
			event.deferEdit().queue();
		}
	};

	protected boolean notInSameVoiceChannel(User u) {

		VoiceChannel requesterChannel = getVoiceChannel(guild.retrieveMember(u).complete());
		VoiceChannel botChannel = getVoiceChannel(guild.getSelfMember());

		return !Objects.equals(requesterChannel, botChannel);
	}

	private VoiceChannel getVoiceChannel(Member m){
		return Objects.requireNonNull(m.getVoiceState()).getChannel();
	}

	public TextChannel getMusicChannel() {
		TextChannel mc = GuildManager.getGuildData(guild).getDefaultMusicChannel();

		if (mc==null){
			mc = lastMusicChannel;
		}

		return mc;
	}

	public void setMusicChannel(TextChannel musicChannel) {
		lastMusicChannel=musicChannel;
	}

	@Override
	public void handleEvent(MusicEvent event) {


		if(event instanceof MusicJoinEvent){
			getMusicChannel().sendMessage(String.format(MusicCommand.JOIN, ((MusicJoinEvent) event).getChannelJoined().getName())).queue();
		}

		else if (event instanceof MusicStartEvent){
			getMusicChannel().sendMessage(
					String.format(MusicCommand.NOW_PLAYING, ((MusicStartEvent) event).getSong().getInfo().uri)
			).setActionRow(
					Button.primary("ml_pause","Pause"),
					Button.primary("ml_skip","Skip"),
					Button.danger("ml_stop", "Stop")
			).queue(message -> {
				if (nowPlayingMessage!=null){
					clearButtons(nowPlayingMessage);
				}
				nowPlayingMessage=message;
			});
		}

		else if (event instanceof MusicSkipEvent){
			getMusicChannel().sendMessage("Skipping the rest of the Current song :stuck_out_tongue:").queue();
		}

		else if (event instanceof MusicLoadEvent){
			getMusicChannel().sendMessage(String.format("Loaded %s successfully\n%s",
					((MusicLoadEvent) event).getSong().getInfo().title, ((MusicLoadEvent) event).getSong().getInfo().uri)).queue();
		}

		else if (event instanceof PlaylistLoadEvent){
			getMusicChannel().sendMessage(String.format("Loaded Playlist: %s",
					((PlaylistLoadEvent) event).getList().getName())).queue();
		}

		else if (event instanceof MusicPausedEvent) {
			if (!((MusicPausedEvent) event).getPaused()) {
				getMusicChannel().sendMessage(String.format("Music paused. call `%sm play` or `%sm pause` to resume",
						BotCommand.getPrefix(getMusicChannel().getGuild()),
						BotCommand.getPrefix(getMusicChannel().getGuild()))).queue();
			}

		}

		else if (event instanceof VolumeChangeEvent) {
			MessageBuilder mb = new MessageBuilder();

			mb.append(String.format("Volume set to %d\n",((VolumeChangeEvent) event).getVol()))
			.append("```\n")
			.append("*-------------------------*--boost---*\n")
			.append(volumeBar(((VolumeChangeEvent) event).getVol())).append("\n")
			.append("*-------------------------*----------*\n")
			.append("```");


			getMusicChannel().sendMessage(mb.build()).queue();

		}

		else if (event instanceof ShuffleEvent){
			getMusicChannel().sendMessage("*throws all the tracks up in the air....*").queue();
		}

		else if (event instanceof LoadFailedEvent){
			getMusicChannel().sendMessage(String.format("There was a problem loading that song sorry!\n(%s)", ((LoadFailedEvent) event).getException().getMessage())).queue();
		}

		else if (event instanceof NoMatchEvent){
			getMusicChannel().sendMessage("I'm sorry, but i didn't find anything matching that search").queue();
		}

		else if (event instanceof PermErrorEvent){
			getMusicChannel().sendMessage(
					String.format("I'm sorry I dont have permission to join %s\n"
							+ "(If this is in error insure I have `%s` and `Speak` in this channel)",
							((PermErrorEvent) event).getVoiceChannel().getName(),
							((PermErrorEvent) event).getException().getPermission().getName())
			).queue();
		}

		else{
			getMusicChannel().sendMessage("This music event isn't hanndled yet.... Yell at ranzer ("+event.getClass().getSimpleName()+")").queue();

		}


	}

	private void clearButtons(Message message) {
		message.editMessage(message.getContentRaw()).setActionRows().queue();
	}

	private CharSequence volumeBar(int vol) {
		StringBuilder rtn = new StringBuilder();
		rtn.append("*|");

		//not boosted
		if (vol<=100) {

			//number of bars to add (if the math comes out to neg set to 0
			int volBars = Math.max(((vol / 4) - 2), 0);

			//add bars
			for (int i = 0; i < volBars; i++) {
				rtn.append('=');
			}
			rtn.append('|');

			//add blank space
			for (int i = 0; i < 23 - volBars; i++) {
				rtn.append(' ');
			}

			//fill out blank boost space
			rtn.append("*          *");

		//boosted volume
		} else {
			int boost = vol-100;
			int boostBars = boost/5;

			//fill in full standard bar
			rtn.append("========================*");

			//add boost bars
			for (int i = 0; i<boostBars-1;i++){
				rtn.append('=');
			}
			rtn.append("|");

			//add blank space
			for (int i = 0; i<10-boostBars;i++){
				rtn.append(" ");
			}
			rtn.append('*');
		}
		return rtn.toString();
	}
}