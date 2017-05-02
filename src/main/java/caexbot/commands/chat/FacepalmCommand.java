package caexbot.commands.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.commands.DraconicCommand;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FacepalmCommand extends CaexCommand implements DraconicCommand, Describable{

	private String[] facepalms ={
			"*%s is ashamed for you*",
			"*%s shoves their palm through their brain*",
			"*%s slaps their face with a thunderous clap*",
			"*%s tried to high-five the back of their face*",
			"*%s groans as a flat palm meets their forhead*",
			"*%s throws their head on their desk with a loud thud*",
			"*%s thinks you deserve a high five... to the face... with a brick*",
			"*%s sighs and slowly places their palm on their face and shakes their head*",
			"*%s is having an aneurysm trying to comprehend the enormity of your stupidity*"
	};
	
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
	
		if (ThreadLocalRandom.current().nextInt(2)==0) {
			channel.sendMessage(String.format(facepalms[ThreadLocalRandom.current().nextInt(facepalms.length)],
					author.getAsMention())).queue();
		} else {
			
			
			File fp = getResourceAsFile(String.format("/fp_st_%02d.jpg", ThreadLocalRandom.current().nextInt(13)));
			
			if(fp==null){
//				System.out.println("is null");
				return;
			}
			try {
				channel.sendFile(fp, new MessageBuilder().append(author.getAsMention()).build()).complete();;
				fp.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
		StringBuilder sb = new StringBuilder();
		
		sb.append(getShortDescription()).append("\n\n");
		sb.append("Caex will pull a random facepalming emote from his hat and serve it up for you...\ngroan to your heart's content");
		
		return sb.toString();
	}
	@Override
	public String getUsage(Guild g) {
		return String.format("`%s%s`", getPrefix(g),getName());
	}

	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("ehaism_cha'sid");
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}
	
	private static File getResourceAsFile(String path){
		try {
			InputStream in =FacepalmCommand.class.getResourceAsStream(path);
	        System.out.println(path);
	        if (in == null) {
	        	
//	        	System.out.println("in is null");
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

}
   