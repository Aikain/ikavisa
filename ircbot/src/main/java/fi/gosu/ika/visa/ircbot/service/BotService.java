package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Aikain on 15.10.2016.
 */
@Service
public class BotService {

    @Autowired
    private ConfigService configService;
    private Bot bot;

    @PostConstruct
    public void start() {
        bot = new Bot(configService.getConfig());
    }
}
