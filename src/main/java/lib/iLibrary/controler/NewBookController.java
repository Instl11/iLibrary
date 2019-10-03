package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/newBook")
public class NewBookController {

    private BookRepo bookRepo;

    @Autowired
    public NewBookController(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }


    @GetMapping
    public String newBookPage(Model model){
        model.addAttribute("newBook", new Book());
        return "newBook";
    }

    @PostMapping
    public String addBook(@ModelAttribute Book newBook){
        bookRepo.save(newBook);
        return "redirect:/books";
    }


}
