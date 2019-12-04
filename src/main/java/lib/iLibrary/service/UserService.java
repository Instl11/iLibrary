package lib.iLibrary.service;

import lib.iLibrary.entity.RegistrationForm;
import lib.iLibrary.entity.Role;
import lib.iLibrary.entity.User;
import lib.iLibrary.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private CustomMailSender mailSender;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, CustomMailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User " + username + " not found");
    }

    public boolean addUser(RegistrationForm form) {
        User userFromDB = userRepo.findByUsername(form.getUsername());
        if (userFromDB != null) {
            return false;
        }
        User user = form.toUser(passwordEncoder);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s! \n" +
                            "Please visit link to activate your account: http://localhost:8080/activate/%s",
                    user.getFullName(), user.getActivationCode());

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }
}