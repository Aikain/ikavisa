package fi.gosu.ika.visa.ircbot.repository;

import fi.gosu.ika.visa.ircbot.domain.TietovisaPiste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Ville on 18.12.2016.
 */
public interface TietovisaPisteRepository extends JpaRepository<TietovisaPiste, Long> {

    TietovisaPiste findByUsername(String username);

    List<TietovisaPiste> findAllOrderByPointsDesc();
}
