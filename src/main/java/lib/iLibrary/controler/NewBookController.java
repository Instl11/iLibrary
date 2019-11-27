package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/newBook")
public class NewBookController {

    private BookService bookService;

    public NewBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String newBookPage(Model model) {
        model.addAttribute("newBook", new Book());
        return "newBook";
    }

    @PostMapping
    public String addBook(@ModelAttribute Book newBook,
                          @RequestParam("file") MultipartFile file) throws IOException {
        bookService.addFileAndSave(newBook, file);
        return "redirect:/books";
    }
}
