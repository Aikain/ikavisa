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
        if (message.charAt(0) == '!' || message.startsWith(bot.getName() + ":")) {
            String cmd = (message.charAt(0) == '!' ? message.substring(1) : message.substring(bot.getName().length() + 1)).trim();
            String[] args = {};
            if (cmd.indexOf(" ") != -1) {
                args = cmd.substring(cmd.indexOf(" ")+1).split(" ");
                cmd = cmd.substring(0, cmd.indexOf(" "));
            }
            handleCommand(channel, sender, login, hostname, cmd, args);
        } else {
            handleMessage(channel, sender, login, hostname, message);
        }
    }

    private void handleCommand(String channel, String sender, String login, String hostname, String command, String[] args) {
        switch (command.toLowerCase()) {
            case "kaynnista":
            case "käynnistä":
                bot.startGame(channel, sender, login, hostname, args);
                break;
            case "lopeta":
                bot.stopGame();
                bot.clearGame();
                break;
            case "nollaa":
            case "reset":
                if (bot.getGame() != null) bot.getGame().reset();
                break;
            case "restart":
                bot.restart();
            case "stop":
                bot.quit();
                break;
            case "join":
                for (String arg : args) bot.join(arg);
                break;
            case "leave":
                for (String arg : args) bot.leave(arg);
                break;
            default:
                if (bot.getGame() != null) bot.getGame().command(channel, sender, login, hostname, command, args);
        }
    }

    private void handleMessage(String channel, String sender, String login, String hostname, String message) {
        switch (message.toLowerCase()) {
            default:
                if (bot.getGame() != null) bot.getGame().message(channel, sender, login, hostname, message);

        }
    }

}
