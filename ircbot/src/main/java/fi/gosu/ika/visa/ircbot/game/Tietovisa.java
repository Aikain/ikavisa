package fi.gosu.ika.visa.ircbot.game;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import fi.gosu.ika.visa.ircbot.bot.Game;
import fi.gosu.ika.visa.ircbot.domain.User;

/**
 * Created by Aikain on 16.12.2016.
 */
public class Tietovisa implements Game {
    private boolean run;
    private Bot bot;
    private String channel;

    @Override
    public void start(Bot bot, String channel, User user, String[] args) {
        this.bot = bot;
        this.channel = channel;
        this.run = true;
        simpleHelp();
    }

    @Override
    public void stop() {
        this.run = false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void simpleHelp() {
        bot.sendMessage(channel, "Tervetuloa tietovisailemaan!");
    }

    @Override
    public void help() {
        bot.sendMessage(channel, "Vastaa kysymyksiin Tomppa!");
    }

    @Override
    public void message(String channel, User user, String message) {

    }

    @Override
    public void command(String channel, User user, String command, String[] args) {

    }
}