package lib.iLibrary.entity;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String fullName;

    public User toUser(PasswordEncoder encoder) {
        return new User(username, encoder.encode(password), fullName);
    }
}
