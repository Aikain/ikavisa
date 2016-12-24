package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.domain.TietovisaPiste;
import fi.gosu.ika.visa.ircbot.domain.User;
import fi.gosu.ika.visa.ircbot.repository.TietovisaPisteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ville on 18.12.2016.
 */
@Service
public class PointService {

    @Autowired
    private TietovisaPisteRepository tietovisaPisteRepository;

    public boolean addPoint(User user) {
        boolean added = false;
        TietovisaPiste tietovisaPiste = tietovisaPisteRepository.findByUsername(user.getName());
        if (tietovisaPiste == null) {
            tietovisaPiste = new TietovisaPiste(user.getName(), 0);
        }
        if (!isSameDay(tietovisaPiste.getLastPoint(), new Date(System.currentTimeMillis()))) {
            tietovisaPiste.setDateCount(0);
        }
        if (tietovisaPiste.getDateCount() < 20 && tietovisaPiste.getPoints() < 50) {
            tietovisaPiste.addPoint();
            tietovisaPiste.addDateCount();
            added = true;
        }
        tietovisaPisteRepository.save(tietovisaPiste);
        return added;
    }

    public List<TietovisaPiste> getAllPoints() {
        return tietovisaPisteRepository.findAllByOrderByPointsDesc();
    }

    public void resetAll() {
        tietovisaPisteRepository.deleteAll();
    }

    //TODO: siirrä toolseihin tms
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) throw new IllegalArgumentException("The dates must not be null");
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null)  throw new IllegalArgumentException("The dates must not be null");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
}
