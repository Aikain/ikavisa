package fi.gosu.ika.visa.ircbot;

import javax.xml.bind.JAXBException;
import java.util.Scanner;

/**
 * Created by Aikain on 13.10.2016.
 */
public class IrcBot {

    public static void main(String[] args) {
        ReadXml<Config> xmlBMMaster = new ReadXml<>(Config.class, "src/main/resources/config.xml");
        try {
            Bot bot = new Bot(xmlBMMaster.getObj());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
