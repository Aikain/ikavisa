package fi.gosu.ika.visa.ircbot.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

/**
 * Created by Aikain on 15.10.2016.
 */
@Entity
public class User extends AbstractPersistable<Long> {
    private String name;
    private String hostname;
    @Enumerated(EnumType.STRING)
    private ROLE role;
    @Transient private String sender;
    @Transient private String login;

    public User() {
        this.role = ROLE.DEFAULT;
    }

    public User(String name, String hostname) {
        this.name = name;
        this.hostname = hostname;
        this.role = ROLE.DEFAULT;
    }

    public User(String name, String hostname, ROLE role) {
        this.name = name;
        this.hostname = hostname;
        this.role = role;
    }

    public String getName() {
        return name;
    }
    public String getHostname() {
        return hostname;
    }
    public ROLE getRole() {
        return role;
    }
    public String getSender() {
        return sender;
    }
    public String getLogin() {
        return login;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public void setRole(ROLE role) {
        this.role = role;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public enum ROLE {
        ADMIN, GM, PLAYER, DEFAULT
    }
}
