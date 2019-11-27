package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.exceptions.NoCurrentBookException;
import lib.iLibrary.repository.BookRepository;
import lib.iLibrary.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
        try {
            model.addAttribute("editedBook", bookService.getById(id));
        } catch (NoCurrentBookException e) {
            return "noCurrentBook";
        }
        return "editForm";
    }

    @PostMapping("/{id}")
    public String editBook(@ModelAttribute Book editedBook,
                           @RequestParam("file") MultipartFile file) throws IOException {
        Book book;
        try {
            book = bookService.getById(editedBook.getId());
        } catch (NoCurrentBookException e) {
            return "noCurrentBook";
        }
        BeanUtils.copyProperties(editedBook, book, "id", "creationDate", "fileName");

        bookService.addFileAndSave(book, file);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") Long id) {
        Book book;
        try {
            book = bookService.getById(id);
        } catch (NoCurrentBookException e) {
            return "noCurrentBook";
        }
        bookService.deleteFile(book);
        return "redirect:/edit/" + id;
    }
}
