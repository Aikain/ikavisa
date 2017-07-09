package fi.gosu.ika.visa.ircbot.repository;

import fi.gosu.ika.visa.ircbot.domain.HirsipuuPiste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Ville on 09.07.2017.
 */
public interface HirsipuuPisteRepository extends JpaRepository<HirsipuuPiste, Long> {

    HirsipuuPiste findByUsername(String username);

    List<HirsipuuPiste> findAllByOrderByPointsDesc();
}
