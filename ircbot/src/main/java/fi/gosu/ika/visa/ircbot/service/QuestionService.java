package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.domain.Question;
import fi.gosu.ika.visa.ircbot.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Aikain on 23.12.2016.
 */
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question add(String s) {
        int i = s.lastIndexOf("--");
        Question question = new Question();
        if (i == -1) {
            i = s.lastIndexOf("?");
            question.setQuest(s.substring(0, i+1).trim());
            question.setAnswer(s.substring(i+1).trim());
        } else {
            question.setQuest(s.substring(0, i).trim());
            question.setAnswer(s.substring(i+2).trim());
        }
        question.setLastAsked(new Date(0));
        return questionRepository.save(question);
    }

    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    public Question getRandomQuestion() {
        Pageable oldest = new PageRequest(0, 10, Sort.Direction.ASC, "lastAsked");
        Page<Question> questions = questionRepository.findAll(oldest);
        Question question = questions.getContent().get((int)(Math.random() * questions.getContent().size()));
        question.setLastAsked(new Date(System.currentTimeMillis()));
        return questionRepository.save(question);
    }

    public Question get(Long id) {
        return questionRepository.getOne(id);
    }

    public List<Question> getAll() {
        return questionRepository.findAll();
    }

    public boolean updateQuestion(Long id, String newQuest) {
        if (newQuest.isEmpty()) return false;
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (!optionalQuestion.isPresent()) return false;
        Question question = optionalQuestion.get();
        question.setQuest(newQuest);
        questionRepository.save(question);
        return true;
    }
}
