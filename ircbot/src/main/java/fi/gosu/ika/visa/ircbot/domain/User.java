package fi.gosu.ika.visa.ircbot.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by Aikain on 15.10.2016.
 */
@Entity
public class User extends AbstractPersistable<Long> {
    private String name;
    private String hostname;
    @Enumerated(EnumType.STRING)
    private ROLE role;

    public User() {
        this.role = ROLE.DEFAULT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    private enum ROLE {
        ADMIN, GM, PLAYER, DEFAULT
    }
}
