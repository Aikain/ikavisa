package fi.gosu.ika.visa.ircbot.game;

import fi.gosu.ika.visa.ircbot.Bot;
import fi.gosu.ika.visa.ircbot.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aikain on 14.10.2016.
 */
public class Matikka implements Game {
    private Bot bot;
    private String channel;
    private int first, second;
    private char operator;
    private Map<String, Integer> points;

    @Override
    public void start(Bot bot, String channel, String sender, String login, String hostname, String[] args) {
        this.bot = bot;
        this.channel = channel;
        this.points = new HashMap<>();
        generateNew();
    }

    @Override
    public void stop() {

    }

    @Override
    public void reset() {
        generateNew();
        simpleHelp();
    }

    @Override
    public void simpleHelp() {
        bot.sendMessage(channel, "Tervetuloa matikka-peliin!");
        bot.sendMessage(channel, "Tehtävänäsi on ratkaista yksinkertaisia laskutehtäviä. Onnea kaikille!");
        ask();
    }

    @Override
    public void help() {
        bot.sendMessage(channel, "Tehtävänäsi on ratkaista yksinkertaisia laskutehtäviä.");
        bot.sendMessage(channel, "Voit tarkistaa pistetilanteen komenolla: !tilanne");
    }

    @Override
    public void message(String channel, String sender, String login, String hostname, String message) {
        try {
            if (check(Integer.parseInt(message))) {
                bot.sendMessage(channel, "Oikein! Piste " + sender + ":lle.");
                int value = this.points.containsKey(sender) ? this.points.get(sender) : 0;
                this.points.put(sender, value + 1);
                generateNew();
                ask();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void command(String channel, String sender, String login, String hostname, String command, String[] args) {
        switch (command) {
            case "tilanne":
            case "info":
            case "status":
                bot.sendMessage(this.channel, "Pistetilanne: ");
                for (Map.Entry<String, Integer> entry : this.points.entrySet()) {
                    bot.sendMessage(this.channel, " " + entry.getKey() + ": " + entry.getValue() );
                }
        }
    }

    private void ask() {
        bot.sendMessage(channel, "Paljon on " + first + " " + operator + " " + second + " ?");
    }

    private void generateNew() {
        first = (int)(Math.random()*20);
        second = (int)(Math.random()*20);
        switch ((int)(Math.random()*4)) {
            case 0:
                operator = '+';
                break;
            case 1:
                operator = '-';
                break;
            case 2:
                operator = '*';
                break;
            case 3:
                if (second == 0) {
                    generateNew();
                    return;
                }
                operator = '/';
                first *= second;
                break;
        }
    }

    private boolean check(int tulos) {
        switch (operator) {
            case '+':
                return tulos == first + second;
            case '-':
                return tulos == first - second;
            case '*':
                return tulos == first * second;
            case '/':
                return tulos == first / second;
        }
        return false;
    }
}
