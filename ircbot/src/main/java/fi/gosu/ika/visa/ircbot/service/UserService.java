package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.domain.User;
import fi.gosu.ika.visa.ircbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Aikain on 15.10.2016.
 */
@Service
public class UserService {

    @Value("${bot.admin.name}")
    private String adminName;
    @Value("${bot.admin.hostname}")
    private String adminHostName;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        for (User user : userRepository.findAll()) {
            if (user.getRole() == User.ROLE.ADMIN) return;
        }
        userRepository.save(new User(adminName, adminHostName, User.ROLE.ADMIN));
    }

    public User getUser(String hostname, String sender, String login) {
        User user = userRepository.findByHostname(hostname);
        if (user == null) user = new User();
        user.setSender(sender);
        user.setLogin(login);
        user.setHostname(hostname);
        return user;
    }

    public User createUser(String name, String hostname, String role, User creator) {
        User user = new User(name, hostname);
        switch (role.toLowerCase()) {
            case "admin":
                if (creator.getRole() != User.ROLE.ADMIN) return null;
                user.setRole(User.ROLE.ADMIN);
                break;
            case "gm":
                if (creator.getRole() != User.ROLE.ADMIN && creator.getRole() != User.ROLE.GM) return null;
                user.setRole(User.ROLE.GM);
                break;
            case "player":
                if (creator.getRole() != User.ROLE.ADMIN && creator.getRole() != User.ROLE.GM && creator.getRole() != User.ROLE.PLAYER) return null;
                user.setRole(User.ROLE.PLAYER);
                break;
        }
        return userRepository.save(user);
    }

}
