package fi.gosu.ika.visa.ircbot.bot;

/**
 * Created by Aikain on 14.10.2016.
 */
public interface Game {
    public void start(Bot bot, String channel, String sender, String login, String hostname, String[] args);
    public void stop();
    public void reset();
    public void simpleHelp();
    public void help();
    public void message(String channel, String sender, String login, String hostname, String message);
    public void command(String channel, String sender, String login, String hostname, String command, String[] args);
}
