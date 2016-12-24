package fi.gosu.ika.visa.ircbot.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by Ville on 18.12.2016.
 */
@Entity
public class TietovisaPiste extends AbstractPersistable<Long> {

    private String username;
    private Integer points;
    private Date lastPoint;
    private Integer dateCount;

    public TietovisaPiste() {
        this.points = 0;
        this.dateCount = 0;
        this.lastPoint = new Date(0);
    }

    public TietovisaPiste(String username, Integer points) {
        this.username = username;
        this.points = points;
        this.dateCount = 0;
        this.lastPoint = new Date(0);
    }

    public String getUsername() {
        return username;
    }
    public Integer getPoints() {
        return points;
    }
    public Date getLastPoint() {
        return lastPoint != null ? lastPoint : new Date(0);
    }
    public Integer getDateCount() {
        return dateCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPoints(Integer points) {
        this.points = points;
    }
    public void setLastPoint(Date lastPoint) {
        this.lastPoint = lastPoint;
    }
    public void setDateCount(Integer dateCount) {
        this.dateCount = dateCount;
    }

    public void addPoint() {
        this.points++;
        this.lastPoint = new Date(System.currentTimeMillis());
    }
    public void addDateCount() {
        this.dateCount++;
    }
}
