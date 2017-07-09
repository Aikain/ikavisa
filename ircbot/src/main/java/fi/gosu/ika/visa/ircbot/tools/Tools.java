package fi.gosu.ika.visa.ircbot.tools;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import fi.gosu.ika.visa.ircbot.domain.Piste;

import java.util.List;

/**
 * @author Ville Nupponen
 * @since .
 */
public class Tools {

    public static void printPoints(Bot bot, String channel, String[] args, List<? extends Piste> tietovisaPisteList) {
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
    }
}
