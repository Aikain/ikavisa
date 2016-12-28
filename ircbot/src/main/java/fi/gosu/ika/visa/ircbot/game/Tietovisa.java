package fi.gosu.ika.visa.ircbot.game;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import fi.gosu.ika.visa.ircbot.bot.Game;
import fi.gosu.ika.visa.ircbot.domain.Question;
import fi.gosu.ika.visa.ircbot.domain.TietovisaPiste;
import fi.gosu.ika.visa.ircbot.domain.User;
import fi.gosu.ika.visa.ircbot.tools.LockMatch;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if (checkAnswer(message, user)) {
            pickRandomQuestion();
            counter++;
            if (counter < 10 + (int)(Math.random() * 30)) {
                wait((10 + (int) (Math.random() * 50)));
            } else {
                counter = 0;
                int min = 15 + (int)(Math.random()*45);
                bot.sendMessage(this.channel, "Pidetään " + min + " minuutin happihyppely!");
                wait(min * 60);
            }
            stop();
        }
    }

    public boolean checkAnswer(String answer, User user){
        answer = answer.toLowerCase().trim();
        int p = LockMatch.lock_match(answer, currentQuestion.getAnswer());
        if (p > 80 || (p > 70 && (currentQuestion.getAnswer().length() == 4 || currentQuestion.getAnswer().length() == 5))) {
            String msg = p == 100 ? "Oikein!" : "Ei ihan oikea vastaus, mutta " + p + "% oikein! Oikea vastaus: " + currentQuestion.getAnswer() + ".";
            bot.sendMessage(this.channel, msg + (bot.getPointService().addPoint(user) ? " Piste " + user.getName() + ":lle!" : ""));
            return true;
        }
        return false;
    }

    private void wait(int min) {
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    run = true;
                    ask();
                }
            },
            min * 1000
        );
    }

    private void pickRandomQuestion() {
        this.currentQuestion = bot.getQuestionService().getRandomQuestion();
    }

    private void ask() {
        if (!run) return;
        bot.sendMessage(this.channel, "#" + this.currentQuestion.getId() + ") " + this.currentQuestion.getQuest());
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
                        if (args.length > 1 && args[1].equals("all")) bot.sendMessage(channel, "Vastaus: " + question.getAnswer());
                        bot.sendMessage(channel, "Kysytty viimeeksi: " + question.getLastAsked());
                        return;
                    case "lisaa":
                    case "lisää":
                        question = bot.getQuestionService().add(String.join(" ", args));
                        bot.sendMessage(channel, "Kysymys #" + question.getId() + " lisätty!");
                        return;
                    case "paivita":
                    case "päivitä":
                        if (bot.getQuestionService().updateQuestion(Long.parseLong(args[0]), Arrays.stream(args).skip(1).collect(Collectors.joining(" "))))
                            bot.sendMessage(channel, "Kysymys #" + args[0] + " päivitetty!");
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
                        if (args.length == 0) {
                            if (tietovisaPisteList.isEmpty()) {
                                bot.sendMessage(channel, "Ei pisteitä kenelläkään!");
                                return;
                            }
                            for (int i = 0; i < tietovisaPisteList.size() && i < 3; i++) {
                                bot.sendMessage(channel, (i + 1) + ") " + tietovisaPisteList.get(i).getUsername() + ": " + tietovisaPisteList.get(i).getPoints() + " pistettä");
                            }
                            String others = "";
                            for (int i = 3; i < tietovisaPisteList.size(); i++) {
                                others += tietovisaPisteList.get(i).getUsername() + " (" + tietovisaPisteList.get(i).getPoints() + "p)" + ", ";
                            }
                            if (!others.isEmpty())
                                bot.sendMessage(channel, "Muille sijoille päässeet: " + others.substring(0, others.length() - 2));
                        } else if (args.length <= 3){
                            for (String name : args) {
                                int i = 0;
                                for (; i < tietovisaPisteList.size(); i++) {
                                    if (tietovisaPisteList.get(i).getUsername().equals(name)) {
                                        bot.sendMessage(channel, (i + 1) + ") " + tietovisaPisteList.get(i).getUsername() + ": " + tietovisaPisteList.get(i).getPoints() + " pistettä");
                                        break;
                                    }
                                }
                                if (i == tietovisaPisteList.size())
                                    bot.sendMessage(channel,"Ei pisteitä pelaajalla '" + name + "'");
                            }
                        } else {
                            bot.sendMessage(channel, "Voin kertoa maksimissaan kolmen käyttäjän pisteet kerralla.");
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
