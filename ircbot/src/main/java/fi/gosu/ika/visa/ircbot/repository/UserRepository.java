package fi.gosu.ika.visa.ircbot.repository;

import fi.gosu.ika.visa.ircbot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Aikain on 15.10.2016.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByHostname(String hostname);
}
