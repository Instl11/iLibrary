package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.entity.User;
import lib.iLibrary.repository.UserRepository;
import lib.iLibrary.service.BookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/newBook")
public class NewBookController {

    private BookService bookService;
    private UserRepository userRepo;

    public NewBookController(BookService bookService, UserRepository userRepo) {
        this.bookService = bookService;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String newBookPage(Book book) {
        return "newBook";
    }

    @PostMapping
    public String addBook(@Valid Book book,
                          BindingResult bindingResult,
                          @RequestParam("file") MultipartFile file,
                          @AuthenticationPrincipal User user) throws IOException {

        if (bindingResult.hasErrors()) {
            return "newBook";
        }

        Book savedBook = bookService.addFileAndSave(book, file);

        user.getBookList().add(savedBook);
        userRepo.save(user);
        return "redirect:/books";
    }
}
