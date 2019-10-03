package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.exceptions.NoCurrentBookException;
import lib.iLibrary.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BooksController {

    private BookRepo bookRepo;

    @Autowired
    public BooksController(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping("/")
    public String getStartPage(){
        return "greeting";
    }

    @GetMapping("/books")
    public String getBooks(Model model){

        PageRequest page = PageRequest.of(0, 5, Sort.by("creationDate").descending() );
        List<Book> books = bookRepo.findAll(page).getContent();
        model.addAttribute("booksList", books);

        PageRequest popular = PageRequest.of(0,3, Sort.by("mark").descending());
        List<Book> popularBooks = bookRepo.findAll(popular).getContent();
        model.addAttribute("popular", popularBooks);
        return "booksList";
    }

    @PostMapping("/books")
    public String newBook(){
        return "redirect:/newBook";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        bookRepo.deleteById(id);
        return "redirect:/books";
    }



}
