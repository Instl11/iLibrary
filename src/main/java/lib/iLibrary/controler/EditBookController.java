package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.exceptions.NoCurrentBookException;
import lib.iLibrary.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/edit")
public class EditBookController {

    private BookRepo bookRepo;

    @Autowired
    public EditBookController(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping("/{id}")
    public String editBookPage(@PathVariable("id") Long id, Model model){

        try {
            Book book = bookRepo.findById(id).orElseThrow(NoCurrentBookException::new);
            model.addAttribute("editedBook", book);
        }catch (NoCurrentBookException e){
            return "noCurrentBook";
        }
        return "editForm";
    }

    @PostMapping("/{id}")
    public String editBook(@ModelAttribute Book editedBook){
        bookRepo.save(editedBook);
        return "redirect:/books";
    }
}
