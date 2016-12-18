package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.domain.TietovisaPiste;
import fi.gosu.ika.visa.ircbot.repository.TietovisaPisteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ville on 18.12.2016.
 */
@Service
public class PointService {

    @Autowired
    private TietovisaPisteRepository tietovisaPisteRepository;

    public void addPoint(String username) {
        TietovisaPiste tietovisaPiste = tietovisaPisteRepository.findByUsername(username);
        if (tietovisaPiste == null) {
            tietovisaPiste = new TietovisaPiste(username, 0);
        }
        tietovisaPiste.addPoint();
        tietovisaPisteRepository.save(tietovisaPiste);
    }

    public List<TietovisaPiste> getAllPoints() {
        return tietovisaPisteRepository.findAllOrderByPoints();
    }
}
