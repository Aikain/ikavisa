package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.tools.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Aikain on 15.10.2016.
 */
@Service
public class ConfigService {

    @Value("${bot.connect.name}")
    private String name;
    @Value("${bot.connect.login}")
    private String login;
    @Value("${bot.connect.debug}")
    private String debug;
    @Value("${bot.auth.name}")
    private String authName;
    @Value("${bot.auth.pass}")
    private String authPass;
    @Value("${bot.auth.cmd}")
    private String authCmd;
    @Value("${bot.connect.server}")
    private String server;
    @Value("${bot.connect.channels}")
    private String channels;

    public Config getConfig() {
        Config config = new Config();
        config.setLogin(login);
        config.setName(name);
        config.setDebug("true".equals(debug));
        config.setLoginName(authName);
        config.setLoginPass(authPass);
        config.setLoginCmd(authCmd);
        config.setServer(server);
        config.setChannels(channels != null ? Arrays.asList(channels.split("\\s*,\\s*")) : Collections.emptyList());
        return config;
    }
}
