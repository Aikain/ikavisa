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

/**
 * Created by Aikain on 23.12.2016.
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

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
        questionRepository.delete(id);
    }

    public Question getRandomQuestion() {
        Pageable oldest = new PageRequest(0, 1, Sort.Direction.ASC, "lastAsked");
        Page<Question> questions = questionRepository.findAll(oldest);
        Question question = questions.getContent().get((int)(Math.random() * questions.getTotalElements()));
        System.out.println("Valitaan kysymys #" + question.getId());
        question.setLastAsked(new Date(System.currentTimeMillis()));
        return questionRepository.save(question);
    }

    public Question get(Long id) {
        return questionRepository.findOne(id);
    }

    public List<Question> getAll() {
        return questionRepository.findAll();
    }
}
