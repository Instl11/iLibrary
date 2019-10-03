package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.exceptions.NoCurrentBookException;
import lib.iLibrary.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/books")
public class FindBooksController {

    private BookRepo bookRepo;

    @Autowired
    public FindBooksController(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping("/byId")
    public String bookById(@RequestParam Long id, Model model){
        try {
            Book book = bookRepo.findById(id).orElseThrow(NoCurrentBookException::new);
            model.addAttribute("book", book);
        }catch (NoCurrentBookException e){
            return "noCurrentBook";
        }
        return "bookById";
    }

    @GetMapping("/byName")
    public String bookByName(@RequestParam String name, Model model){
        List<Book> byNameStartingWith = bookRepo.findByNameStartingWith(name);
        model.addAttribute("list", byNameStartingWith);
        return "bookByName";
    }

    @GetMapping("/byAuthor")
    public String bookByAuthor(@RequestParam String author, Model model){
        List<Book> byAuthor = bookRepo.getBooksByAuthor(author);
        model.addAttribute("listA", byAuthor);
        return "bookByAuthor";
    }
}
