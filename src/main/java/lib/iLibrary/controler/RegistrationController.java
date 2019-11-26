package lib.iLibrary.controler;

import lib.iLibrary.entity.RegistrationForm;
import lib.iLibrary.entity.User;
import lib.iLibrary.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/registration")
public class RegistrationController {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepo;

    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String registrationForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        User user = form.toUser(passwordEncoder);
        userRepo.save(user);
        return "redirect:/login";
    }
}
