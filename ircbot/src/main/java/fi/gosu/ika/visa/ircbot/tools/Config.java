package fi.gosu.ika.visa.ircbot.tools;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Aikain on 15.10.2016.
 */
@XmlRootElement
public class Config {
    private String name;
    private String login;
    private Boolean debug;
    private String loginName;
    private String loginPass;
    private String loginCmd;
    private String server;
    private List<String> channels;

    public String getName() {
        return name;
    }
    public String getLogin() {
        return login;
    }
    public Boolean getDebug() {
        return debug;
    }
    public String getLoginName() {
        return loginName;
    }
    public String getLoginPass() {
        return loginPass;
    }
    public String getLoginCmd() {
        return loginCmd;
    }
    public String getServer() {
        return server;
    }
    public List<String> getChannels() {
        return channels;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setDebug(Boolean debug) {
        this.debug = debug;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }
    public void setLoginCmd(String loginCmd) {
        this.loginCmd = loginCmd;
    }
    public void setServer(String server) {
        this.server = server;
    }
    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}
