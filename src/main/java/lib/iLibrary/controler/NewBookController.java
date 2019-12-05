package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/newBook")
public class NewBookController {

    private BookService bookService;

    public NewBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String newBookPage(Book book) {
        return "newBook";
    }

    @PostMapping
    public String addBook(@Valid Book book,
                          BindingResult bindingResult,
                          @RequestParam("file") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            return "newBook";
        }

        bookService.addFileAndSave(book, file);
        return "redirect:/books";
    }
}
