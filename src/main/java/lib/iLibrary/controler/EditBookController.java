package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("edit")
public class EditBookController {

    private BookService bookService;

    @Value("${upload.path}")
    private String uploadPath;

    public EditBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public String editBookPage(@PathVariable("id") Long id, Model model) {

        model.addAttribute("book", bookService.getById(id));
        return "editForm";
    }

    @PostMapping("/{id}")
    public String editBook(@Valid Book editedBook,
                           BindingResult bindingResult,
                           Model model,
                           @RequestParam("file") MultipartFile file) throws IOException {
        if (bindingResult.hasErrors()) {
            return "editForm";
        }
        Book book = bookService.getById(editedBook.getId());
        BeanUtils.copyProperties(editedBook, book, "id", "creationDate", "fileName");

        bookService.addFileAndSave(book, file);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") Long id) {

        Book book = bookService.getById(id);
        bookService.deleteFile(book);
        return "redirect:/edit/" + id;
    }
}
