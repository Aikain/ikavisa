package fi.gosu.ika.visa.ircbot.game;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import fi.gosu.ika.visa.ircbot.bot.Game;
import fi.gosu.ika.visa.ircbot.domain.User;

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
    private boolean run;
    private boolean continuee;
    private boolean pause;

    public Matikka() {
        this.points = new HashMap<>();
        this.run = false;
        this.continuee = false;
        this.pause = false;
    }

    @Override
    public void start(Bot bot, String channel, User user, String[] args) {
        if (this.run) return;
        this.bot = bot;
        this.channel = channel;
        this.run = true;
        generateNew();
        simpleHelp();
    }

    @Override
    public void stop() {
        this.run = false;
        this.continuee = true;
        bot.sendMessage(channel, "Kiitos kaikille pelaajille! Onneksi olkoon pärjänneille!");
        tilanne();
    }

    @Override
    public void reset() {
        this.continuee = false;
        this.points.clear();
        generateNew();
        simpleHelp();
    }

    @Override
    public void simpleHelp() {
        bot.sendMessage(channel, "Tervetuloa " + (continuee ? "takaisin " : "") + "matikka-peliin!");
        bot.sendMessage(channel, "Tehtävänäsi on ratkaista yksinkertaisia laskutehtäviä. Onnea kaikille!");
        ask();
    }

    @Override
    public void help() {
        bot.sendMessage(channel, "Tehtävänäsi on ratkaista yksinkertaisia laskutehtäviä.");
        bot.sendMessage(channel, "Voit tarkistaa pistetilanteen komenolla: !tilanne");
    }

    @Override
    public void message(String channel, User user, String message) {
        if (!run) return;
        try {
            if (check(Integer.parseInt(message))) {
                this.pause = true;
                bot.sendMessage(channel, "Oikein! Piste " + user.getSender() + ":lle.");
                int value = this.points.containsKey(user.getSender()) ? this.points.get(user.getSender()) : 0;
                this.points.put(user.getSender(), value + 1);
                setTimeout(this::generateAndAsk, 5000);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void command(String channel, User user, String command, String[] args) {
        switch (command) {
            case "tilanne":
            case "info":
            case "status":
                tilanne();
        }
    }

    private void ask() {
        if (!this.run) return;
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

    private void generateAndAsk() {
        generateNew();
        ask();
        this.pause = false;
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

    private void tilanne() {
        bot.sendMessage(this.channel, "Pistetilanne: ");
        for (Map.Entry<String, Integer> entry : this.points.entrySet()) {
            bot.sendMessage(this.channel, " " + entry.getKey() + ": " + entry.getValue() );
        }
    }

    private static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
}
