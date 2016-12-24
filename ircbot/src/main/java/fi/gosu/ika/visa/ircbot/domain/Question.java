package fi.gosu.ika.visa.ircbot.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by Aikain on 23.12.2016.
 */
@Entity
public class Question extends AbstractPersistable<Long> {

    private String quest;
    private String answer;
    private Date lastAsked;

    public String getQuest() {
        return quest;
    }
    public String getAnswer() {
        return answer;
    }
    public Date getLastAsked() {
        return lastAsked;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }
    public void setAnswer(String answer) {
        this.answer = answer.toLowerCase().trim();
    }
    public void setLastAsked(Date lastAsked) {
        this.lastAsked = lastAsked;
    }
}
