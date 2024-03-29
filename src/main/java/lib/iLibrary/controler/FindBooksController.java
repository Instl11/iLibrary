package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/books")
public class FindBooksController {

    private BookService bookService;

    public FindBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/byName")
    public String bookByName(@RequestParam String name, Model model) {
        List<Book> byNameStartingWith = bookService.getByName(name);
        model.addAttribute("list", byNameStartingWith);
        return "bookByName";
    }

    @GetMapping("/byAuthor")
    public String bookByAuthor(@RequestParam String author, Model model) {
        List<Book> byAuthor = bookService.getByAuthor(author);
        model.addAttribute("listA", byAuthor);
        return "bookByAuthor";
    }
}
