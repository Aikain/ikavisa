package fi.gosu.ika.visa.ircbot.domain;

/**
 * @author Ville Nupponen
 * @since .
 */
public interface Piste {

    String getUsername();
    Integer getPoints();

    void setUsername(String username);
    void setPoints(Integer points);
    void addPoint();
}
