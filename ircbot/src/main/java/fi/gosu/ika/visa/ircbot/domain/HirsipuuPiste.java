package fi.gosu.ika.visa.ircbot.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

/**
 * Created by Ville on 09.07.2017.
 */
@Entity
public class HirsipuuPiste extends AbstractPersistable<Long> implements Piste {

    String username;
    Integer points;

    public HirsipuuPiste() {
    }

    public HirsipuuPiste(String username, Integer points) {
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
        addPoint(1);
    }

    public void addPoint(int i) {
        this.points += i;
    }
}
