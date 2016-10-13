package fi.gosu.ika.visa.ircbot;

/**
 * Created by Aikain on 13.10.2016.
 */
public class MessageHandler {

    private Bot bot;

    public MessageHandler(Bot bot) {
        this.bot = bot;
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.charAt(0) == '!') {
            handleCommand(channel, sender, login, hostname, message.substring(1, message.indexOf(" ")), message.substring(message.indexOf(" ")).split(" "));
        }
    }

    private void handleCommand(String channel, String sender, String login, String hostname, String command, String[] args) {
        switch (command.toLowerCase()) {
            case "stop":
                bot.quit();
            default:
                bot.sendMessage(channel, "Jag vet inte vad jag ska göra");
        }
    }

    private void handleMessage(String channel, String sender, String login, String hostname, String message) {
        switch (message.toLowerCase()) {
            case "moi":
                bot.sendMessage(channel, "Hei " + sender + "!");
        }
    }

}
