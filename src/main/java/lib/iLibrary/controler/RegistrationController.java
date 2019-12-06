package lib.iLibrary.controler;

import lib.iLibrary.dto.CaptchaResponse;
import lib.iLibrary.entity.User;
import lib.iLibrary.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class RegistrationController {

    private UserService userService;
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/registration")
    public String registrationForm(User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          @Valid User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors() || captchaResponse.equals("")){
            model.addAttribute("captchaError", "Please mark the captcha");
            return "registration";
        }
        if (!Objects.equals(user.getPassword(), user.getConfirmPassword())) {
            model.addAttribute("confirm", "Passwords not equal");
            return "registration";
        }

        String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponse response = restTemplate.postForObject(url, null, CaptchaResponse.class);
        if (!Objects.requireNonNull(response).isSuccess()) {
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("mes", "User already exists");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation is not found");
        }
        return "login";
    }
}
