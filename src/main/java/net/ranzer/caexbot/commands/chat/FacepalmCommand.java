package net.ranzer.caexbot.commands.chat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.ranzer.caexbot.util.Logging;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FacepalmCommand extends BotCommand implements DraconicCommand, Describable{

	private final String[] facepalms ={
			"*%s is ashamed for you*",
			"*%s shoves their palm through their brain*",
			"*%s slaps their face with a thunderous clap*",
			"*%s tried to high-five the back of their face*",
			"*%s groans as a flat palm meets their forehead*",
			"*%s throws their head on their desk with a loud thud*",
			"*%s thinks you deserve a high five... to the face... with a brick*",
			"*%s sighs and slowly places their palm on their face and shakes their head*",
			"*%s is having an aneurysm trying to comprehend the enormity of your stupidity*"
	};

	@Override
	public void processSlash(SlashCommandEvent event) {
		event.reply(getFacepalm(event.getUser())).queue();
	}

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		event.getChannel().sendMessage(getFacepalm(event.getAuthor())).queue();
	}

	private Message getFacepalm(User author) {
		MessageBuilder mb = new MessageBuilder();
//		if (ThreadLocalRandom.current().nextInt(2) == 0) {
			mb.append(String.format(facepalms[ThreadLocalRandom.current().nextInt(facepalms.length)],
					author.getAsMention()));
//		} else {
//
//			File fp = getResourceAsFile(String.format("/fp_st_%02d.jpg", ThreadLocalRandom.current().nextInt(13)));
//			mb.setEmb
//
//			event.getChannel().sendMessage(event.getAuthor().getAsMention()).addFile(fp, "fp.jpg").complete();
//			Logging.debug("Facepalm fp.delete call returned: " + fp.delete());
//
//		}
		return mb.build();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("facepalm","fp");
	}

	@Override
	public String getShortDescription() {
		return "Facepalm....";
	}

	@Override
	public String getLongDescription() {
		@SuppressWarnings("StringBufferReplaceableByString")
		StringBuilder sb = new StringBuilder();

		sb.append(getShortDescription()).append("\n\n");
		sb.append("Caex will pull a random facepalming emote from his hat and serve it up for you...\ngroan to your heart's content");

		return sb.toString();
	}
	@Override
	public List<String> getDraconicAlias() {
		return Collections.singletonList("ehaism_cha'sid");
	}
	
	@Override
	public Category getCategory() {
		return Category.CHAT;
	}

	private static File getResourceAsFile(String path){
		try {
			InputStream in =FacepalmCommand.class.getResourceAsStream(path);
	        if (in == null) {
	        	return null;
	        }

	        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".jpg");
	        tempFile.deleteOnExit();

	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            //copy stream
	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = in.read(buffer)) != -1) {
	                out.write(buffer, 0, bytesRead);
	            }
	        }
	        return tempFile;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData(getName(),getShortDescription());
	}
}
   