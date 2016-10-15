package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.tools.Config;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by Aikain on 15.10.2016.
 */
@Service
public class ConfigService {

    private static final String PROPERTY_NAME_BOT_NAME = "bot.name";
    private static final String PROPERTY_NAME_BOT_LOGIN = "bot.login";
    private static final String PROPERTY_NAME_BOT_DEBUG = "bot.debug";
    private static final String PROPERTY_NAME_BOT_AUTH_NAME = "bot.auth.name";
    private static final String PROPERTY_NAME_BOT_AUTH_PASS = "bot.auth.pass";
    private static final String PROPERTY_NAME_BOT_AUTH_CMD = "bot.auth.cmd";
    private static final String PROPERTY_NAME_BOT_SERVER = "bot.server";
    private static final String PROPERTY_NAME_BOT_CHANNELS = "bot.channels";

    @Resource
    private Environment env;

    public Config getConfig() {
        Config config = new Config();
        config.setLogin(env.getRequiredProperty(PROPERTY_NAME_BOT_LOGIN));
        config.setName(env.getRequiredProperty(PROPERTY_NAME_BOT_NAME));
        config.setDebug(env.getRequiredProperty(PROPERTY_NAME_BOT_DEBUG, Boolean.class));
        config.setLoginName(env.getRequiredProperty(PROPERTY_NAME_BOT_AUTH_NAME));
        config.setLoginPass(env.getRequiredProperty(PROPERTY_NAME_BOT_AUTH_PASS));
        config.setLoginCmd(env.getRequiredProperty(PROPERTY_NAME_BOT_AUTH_CMD));
        config.setServer(env.getRequiredProperty(PROPERTY_NAME_BOT_SERVER));
        config.setChannels(Arrays.asList(env.getRequiredProperty(PROPERTY_NAME_BOT_CHANNELS).split("\\s*,\\s*")));
        return config;
    }
}
