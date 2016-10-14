package fi.gosu.ika.visa.ircbot;

/**
 * Created by Aikain on 13.10.2016.
 */
public class IrcBot {

    public static void main(String[] args) {
        System.out.println("Botti käynistetty");
        Bot bot = new Bot("ikavisabot", "ikavisa");
        bot.connect();
        bot.join("#wilitestaa");
    }
}
