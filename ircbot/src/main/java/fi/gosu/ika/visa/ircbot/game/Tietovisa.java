package fi.gosu.ika.visa.ircbot.game;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import fi.gosu.ika.visa.ircbot.bot.Game;
import fi.gosu.ika.visa.ircbot.domain.Question;
import fi.gosu.ika.visa.ircbot.domain.TietovisaPiste;
import fi.gosu.ika.visa.ircbot.domain.User;

import java.util.List;

/**
 * Created by Aikain on 16.12.2016.
 */
public class Tietovisa implements Game {
    private boolean run;
    private Bot bot;
    private String channel;
    private Question currentQuestion;
    private int counter;

    public Tietovisa() {
        this.counter = 0;
    }

    @Override
    public void start(Bot bot, String channel, User user, String[] args) {
        this.bot = bot;
        this.channel = channel;
        this.run = true;
        simpleHelp();
        pickRandomQuestion();
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
        bot.sendMessage(channel, "Esitän satunnaisia kysymyksiä satunnaisin väliajoin. Sinun tehtävänäsi on vastata mahdollisimman nopeasti oikein.");
        bot.sendMessage(channel, "Kaikki vastaukset ovat perusmuodossa, jos olet varma vastauksestasi niin koita toista muotoa. En ymmärrä kuin juuri oikean muodon.");
        bot.sendMessage(channel, "Päivässä voi ansaita maksimissan 20 pistettä ja kokonaispisteet voivat kasvaa maksimissaan 50 pisteeseen.");
        bot.sendMessage(channel, "Onnea kilpailijoille!");
    }

    @Override
    public void message(String channel, User user, String message) {
        if (!run) return;
        if (message.equals(currentQuestion.getAnswer())) {
            bot.sendMessage(this.channel, "Oikein!" + (bot.getPointService().addPoint(user) ? " Piste " + user.getName() + ":lle!" : ""));
            pickRandomQuestion();
            counter++;
            if (counter < 10 + (int)(Math.random() * 30)) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                run = true;
                                ask();
                            }
                        },
                        (10 + (int) (Math.random() * 50)) * 1000
                );
            } else {
                int min = 15 + (int)(Math.random()*45);
                bot.sendMessage(this.channel, "Pidetään " + min + " minuutin happihyppely!");
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                run = true;
                                ask();
                            }
                        },
                        min * 60000
                );
            }
            stop();
        }
    }

    private void pickRandomQuestion() {
        this.currentQuestion = bot.getQuestionService().getRandomQuestion();
    }

    private void ask() {
        if (!run) return;
        bot.sendMessage(this.channel, this.currentQuestion.getQuest());
    }

    @Override
    public void command(String channel, User user, String command, String[] args) {
        Question question;
        switch (user.getRole()) {
            case ADMIN:
                switch (command) {
                    case "resetPoints":
                        bot.getPointService().resetAll();
                        bot.sendMessage(channel, "Kaikki pisteet resetattu!");
                        return;
                }
            case GM:
                switch (command) {
                    case "listaa":
                        String s = "";
                        for (Question q : bot.getQuestionService().getAll()) {
                            s += "#" + q.getId() + ", ";
                        }
                        bot.sendMessage(channel, s.isEmpty() ? "Ei kysymyksiä! Laittakaan Tomppa töihin!" : s.substring(0, s.length()-2));
                        return;
                    case "nayta":
                    case "näytä":
                        question = bot.getQuestionService().get(Long.parseLong(args[0]));
                        bot.sendMessage(channel, "Kysymys #" + question.getId() + ": " + question.getQuest());
                        if (args.length > 1 && args[1].equals("all")) bot.sendMessage(channel, "Vastaus:" + question.getAnswer());
                        bot.sendMessage(channel, "Kysytty viimeeksi: " + question.getLastAsked());
                        return;
                    case "lisaa":
                    case "lisää":
                        question = bot.getQuestionService().add(String.join(" ", args));
                        bot.sendMessage(channel, "Kysymys #" + question.getId() + " lisätty!");
                        return;
                    case "poista":
                        try {
                            bot.getQuestionService().delete(Long.parseLong(args[0]));
                            bot.sendMessage(channel, "Kysymys #" + args[0] + " poistettu!");
                        } catch (Exception e) {}
                        return;
                }
            case DEFAULT:
                switch (command) {
                    case "pisteet":
                        List<TietovisaPiste> tietovisaPisteList = bot.getPointService().getAllPoints();
                        for (TietovisaPiste tietovisaPiste : tietovisaPisteList) {
                            bot.sendMessage(channel, tietovisaPiste.getUsername() + ": " + tietovisaPiste.getPoints() + " pistettä");
                        }
                        if (tietovisaPisteList.isEmpty()) {
                            bot.sendMessage(channel, "Ei pisteitä kenelläkään!");
                        }
                        return;
                    case "kysy":
                    case "kysymys":
                    case "ask":
                        ask();
                        return;
                }
        }
    }
}
