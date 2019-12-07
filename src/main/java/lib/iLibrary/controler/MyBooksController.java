package lib.iLibrary.controler;

import lib.iLibrary.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mybooks")
public class MyBooksController {

    @GetMapping
    public String getMyBooks(@AuthenticationPrincipal User user,
                             Model model) {
        model.addAttribute("myBooks", user.getBookList());
        return "myBooks";
    }
}
