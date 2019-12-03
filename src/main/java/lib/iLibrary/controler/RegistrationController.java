package lib.iLibrary.controler;

import lib.iLibrary.entity.RegistrationForm;
import lib.iLibrary.entity.Role;
import lib.iLibrary.entity.User;
import lib.iLibrary.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

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
    public String processRegistration(RegistrationForm form, Model model) {

        User byUsername = userRepo.findByUsername(form.getUsername());
        if (byUsername != null){
            model.addAttribute("mes", "User already exists");
            return "registration";
        }

        User user = form.toUser(passwordEncoder);
        user.setRoles(Collections.singleton(Role.USER));

        userRepo.save(user);
        return "redirect:/login";
    }
}
