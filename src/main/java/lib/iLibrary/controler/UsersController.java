package lib.iLibrary.controler;

import lib.iLibrary.entity.User;
import lib.iLibrary.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UsersController {

    private UserRepository userRepo;

    public UsersController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String getUsers(Model model){

        Iterable<User> all = userRepo.findAll();

        model.addAttribute("users", all);
        return "users";
    }
}
