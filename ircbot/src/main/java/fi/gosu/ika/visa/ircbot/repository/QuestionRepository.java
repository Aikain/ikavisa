package fi.gosu.ika.visa.ircbot.repository;

import fi.gosu.ika.visa.ircbot.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Aikain on 23.12.2016.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findTop50ByOrderByLastAskedAsc();
}
