package fi.gosu.ika.visa.ircbot.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

/**
 * Created by Ville on 18.12.2016.
 */
@Entity
public class TietovisaPiste extends AbstractPersistable<Long> {

    String username;
    Integer points;

    public TietovisaPiste() {
    }

    public TietovisaPiste(String username, Integer points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void addPoint() {
        this.points++;
    }
}
