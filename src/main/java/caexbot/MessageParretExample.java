package caexbot;

import java.util.List;

import javax.security.auth.login.LoginException;

import caexbot.commands.HelpCommand;
import caexbot.commands.InfoCommand;
import caexbot.commands.PingCommand;
import caexbot.commands.ShutdownCommand;
import caexbot.references.CaexBotReference;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.CommandHandler.SimpleCommand;
import de.btobastian.sdcf4j.Sdcf4jMessage;
import de.btobastian.sdcf4j.handler.JDAHandler;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageEmbedEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class MessageParretExample extends ListenerAdapter{

	/**
     * Used for the internal test bot.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        try
        {
          JDA jda = new JDABuilder()
                    .setBotToken(CaexBotReference.TOKEN)
                    .addListener(new MessageParretExample())
                    .buildBlocking();
          jda.getAccountManager().setGame("with Gilmore");

          jda.getTextChannelById("222704069448433674").sendMessage("I LIVE!");
          for (User u : jda.getUsers()) {
			System.out.printf("[%s] [%s]\n",u.getUsername(), u.getId());
		}
          Sdcf4jMessage.MISSING_PERMISSIONS.setMessage("You're not my player! you can't tell me what to do!");
          CommandHandler h = new JDAHandler(jda);
          h.setDefaultPrefix("/");
          h.addPermission("143929240440537089", "player");
          
          h.registerCommand(new HelpCommand(h));
          h.registerCommand(new InfoCommand());
          h.registerCommand(new PingCommand());
          h.registerCommand(new ShutdownCommand());
          
            
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("The config was not populated. Please enter a bot token.");
        }
        catch (LoginException e)
        {
            System.out.println("The provided bot token was incorrect. Please provide valid details.");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageEmbed(MessageEmbedEvent event)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Found embed(s) Types: ");
        event.getMessageEmbeds().stream().forEach(embed -> builder.append(embed.getType()).append(", "));
        System.out.println(builder.toString().substring(0, builder.length() - 2));
    }

    //Note: onMessageReceived combines both the PrivateMessageReceivedEvent and GuildMessageReceivedEvent.
    //If you do not want to capture both in one method, consider using
    // onPrivateMessageReceived(PrivateMessageReceivedEvent event)
    //    or
    // onGuildMessageReceived(GuildMessageReceivedEvent event)
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        boolean isPrivate = event.isPrivate();

        if (!isPrivate)
        {
            System.out.printf("[%s][%s]\t%s: %s\n", event.getGuild().getName(), event.getTextChannel().getName(),
                    event.getAuthor().getUsername(), event.getMessage().getContent());
        }
        else
        {
            System.out.printf("[PM]\t%s: %s\n", event.getAuthor().getUsername(), event.getMessage().getContent());
        }

        //Printing all mentions, if the Message was not private
        if (!isPrivate)
        {
            List<User> mentions = event.getMessage().getMentionedUsers();
            StringBuilder builder = new StringBuilder();
            for (User u : mentions)
            {
                builder.append(u.getUsername()).append(", ");
            }
            String mentionsMessage = builder.toString();
            if (!mentionsMessage.isEmpty())
            {
                mentionsMessage = mentionsMessage.substring(0, mentionsMessage.length() - 2);
                System.out.println("The follow users were mentioned: " + mentionsMessage);
            }
        }
    }
}
