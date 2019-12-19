package fi.gosu.ika.visa.ircbot.game;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import fi.gosu.ika.visa.ircbot.bot.Game;
import fi.gosu.ika.visa.ircbot.domain.User;
import fi.gosu.ika.visa.ircbot.tools.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Ville Nupponen
 * @since 1.1
 */
public class Hirsipuu implements Game {
    private boolean run;
    private Bot bot;
    private String channel;
    private String word;
    private List<Character> correct;
    private List<Character> fails;
    private List<String> words;
    private final int FAILS_MAX_COUNT = 7;

    public Hirsipuu() {
        this.correct = new ArrayList<>();
        this.fails = new ArrayList<>();
        this.words = new ArrayList<>();
        loadWords();
    }

    @Override
    public void start(Bot bot, String channel, User user, String[] args) {
        this.bot = bot;
        this.channel = channel;
        this.run = true;
        simpleHelp();
        pickWord();
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
        bot.sendMessage(channel,"Tervetuloa hirsipuun pariin!");
    }

    @Override
    public void help() {
        bot.sendMessage(channel, "Arvaa kirjain, joko sanomalla pelkkä kirjain tai laittamalla kysymysmerkki perään.");
        bot.sendMessage(channel, "Jokaisesta oikein arvutusta kirjaimesta saa 2pistettä, väärin arvatusta menettää 1pisteen.");
        bot.sendMessage(channel, "Sanan voit arvata kirjoittamalla pelkän sanan tai laittamalla kysymysmerkin perään.");
        bot.sendMessage(channel, "Oikein arvatuista sanoista saa 5pistettä.");
    }

    @Override
    public void message(String channel, User user, String message) {
        if (!run) return;
        message = message.trim().toLowerCase();
        if (message.length() == 1 || (message.length() == 2 && message.charAt(1) == '?')) {
            arvaa(message.charAt(0), user);
        } else if (message.length() >= 2 && !message.contains(" ")) {
            arvaaSana(message.charAt(message.length()-1) == '?' ? message.substring(0, message.length()-1) : message, user);
        }

    }

    @Override
    public void command(String channel, User user, String command, String[] args) {
        switch (user.getRole()) {
            case ADMIN:
                switch (command) {
                    case "resetPoints":
                        bot.getPointService().resetAllHirsipuu();
                        bot.sendMessage(channel, "Kaikki pisteet resetattu!");
                        return;
                }
            case GM:
                switch (command) {
                    default:
                }
            case DEFAULT:
                switch (command) {
                    case "pisteet":
                        Tools.printPoints(bot, channel, args, bot.getPointService().getAllHirsipuuPoints());
                        return;
                }
        }
    }

    private void pickWord() {
        if (words.isEmpty())  {
            bot.sendMessage(channel, "Sanaa ei voitu valita! Sanalista on tyhjä!");
            return;
        }
        correct.clear();
        fails.clear();
        word = words.get((int)(Math.random() * words.size()));
        bot.sendMessage(channel, "Sanaksi arvottiin: " + getHideWord() + ", arvailu alkakoon!");
    }

    private String getHideWord() {
        String s = "";
        for (int i = 0; i < word.length(); i++) {
            s += correct.indexOf(word.charAt(i)) != -1 ? word.charAt(i) : "_";
        }
        return s;
    }

    private void arvaa(char c, User user) {
        if (!correct.contains(c) && !fails.contains(c)) {
            if (word.indexOf(c) != -1) {
                correct.add(c);
                bot.sendMessage(channel,
                        user.getName() + ": Oikein, +2 pistettä. " +
                            "Tiedätte sanasta: " + getHideWord() + ", " +
                            "yrityksiä jäljellä: " + (FAILS_MAX_COUNT - fails.size()) +
                            (fails.isEmpty() ? "" : ", väärin arvatut kirjaimet: " + fails.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()));
                bot.getPointService().addHirsipuuPoint(user, 2);
            } else {
                fails.add(c);
                bot.sendMessage(channel,
                        user.getName() + ": Väärin, -1 pistettä. " +
                            "Tiedätte sanasta: " + getHideWord() + ", " +
                            "yrityksiä jäljellä: " + (FAILS_MAX_COUNT - fails.size()) +
                            ", väärin arvatut kirjaimet: " + fails.stream().map(Object::toString).reduce((acc, e) -> acc + e).get());
                bot.getPointService().addHirsipuuPoint(user, -1);
                if (fails.size() >= FAILS_MAX_COUNT) {
                    run = false;
                    bot.sendMessage(channel,"Hirteen jouduitte! Hetken tauko uutta köyttä laittaessa.");
                    wait(15 + ((int)(Math.random() * 30)));
                }
            }
        }
    }

    private void arvaaSana(String s, User user) {
        if (s.equals(word)) {
            bot.sendMessage(channel, "Sanan arvasi oikein '" + user.getName() + "' ja ansaitsi 5 pistettä!");
            bot.getPointService().addHirsipuuPoint(user, 5);
            run = false;
            bot.sendMessage(channel, "Hetken hengähdystauko ennen seuraavaa sanaa!");
            wait(30 + ((int)(Math.random() * 60)));
        }
    }

    private void wait(int sec) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        run = true;
                        pickWord();
                    }
                },
                sec * 1000
        );
    }

    private void loadWords() {
        Scanner scanner = new Scanner(getClass().getResourceAsStream("sanalista.txt"));
        while (scanner.hasNextLine()) {
            words.add(scanner.nextLine());
        }
    }
}
