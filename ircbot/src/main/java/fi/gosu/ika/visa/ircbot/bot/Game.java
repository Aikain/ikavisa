package fi.gosu.ika.visa.ircbot.bot;

import fi.gosu.ika.visa.ircbot.domain.User;

/**
 * Created by Aikain on 14.10.2016.
 */
public interface Game {
    public void start(Bot bot, String channel, User user, String[] args);
    public void stop();
    public void reset();
    public void simpleHelp();
    public void help();
    public void message(String channel, User user, String message);
    public void command(String channel, User user, String command, String[] args);
}
