package fi.gosu.ika.visa.ircbot.bot;

import fi.gosu.ika.visa.ircbot.domain.User;

/**
 * Created by Aikain on 13.10.2016.
 */
public class MessageHandler {

    private Bot bot;

    public MessageHandler(Bot bot) {
        this.bot = bot;
    }

    public void onMessage(String channel, User user, String message) {
        if (message.charAt(0) == '!' || message.startsWith(bot.getName() + ":")) {
            String cmd = (message.charAt(0) == '!' ? message.substring(1) : message.substring(bot.getName().length() + 1)).trim();
            String[] args = {};
            if (cmd.indexOf(" ") != -1) {
                args = cmd.substring(cmd.indexOf(" ")+1).split(" ");
                cmd = cmd.substring(0, cmd.indexOf(" "));
            }
            handleCommand(channel, user, cmd, args);
        } else {
            handleMessage(channel, user, message);
        }
    }

    private void handleCommand(String channel, User user, String command, String[] args) {
        switch (user.getRole().toString().toLowerCase()) {
            case "admin":
                if (handleAdminCommand(channel, user, command, args)) return;
            case "gm":
                if (handleGMCommand(channel, user, command, args)) return;
            case "player":
            case "default":
                if (handlePlayerAndDefaultCommand(channel, user, command, args)) return;
        }
    }

    private boolean handleAdminCommand(String channel, User user, String command, String[] args) {
        switch (command.toLowerCase()) {
            case "restart":
                bot.restart();
            case "stop":
                bot.quit();
                break;
            case "join":
                for (String arg : args) bot.join(arg);
                break;
            case "leave":
                for (String arg : args) bot.leave(arg, user.getName());
                break;
            case "addlvl":
                if (args.length >= 2) {
                    User newUser = bot.getUserService().createUser(args[0], args[1], args.length > 2 ? args[2] : "", user);
                    bot.sendMessage(channel, "Lisätty käyttäjä " + newUser.getName());
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean handleGMCommand(String channel, User user, String command, String[] args) {
        switch (command.toLowerCase()) {
            case "kaynnista":
            case "käynnistä":
                bot.startGame(channel, user, args);
                break;
            case "pause":
            case "lopeta":
                bot.stopGame();
                break;
            case "nollaa":
            case "reset":
                if (bot.getGame() != null) bot.getGame().reset();
                break;
            default:
                return false;
        }
        return true;
    }
    private boolean handlePlayerAndDefaultCommand(String channel, User user, String command, String[] args) {
        switch (command.toLowerCase()) {
            case "help":
            case "auta":
            case "apua":
                if (bot.getGame() != null) bot.getGame().help();
                break;
            default:
                if (bot.getGame() != null) bot.getGame().command(channel, user, command, args);
        }
        return true;
    }

    private void handleMessage(String channel, User user, String message) {
        switch (message.toLowerCase()) {
            default:
                if (bot.getGame() != null) bot.getGame().message(channel, user, message);

        }
    }

}
