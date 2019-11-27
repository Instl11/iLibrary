package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.exceptions.NoCurrentBookException;
import lib.iLibrary.repository.BookRepository;
import lib.iLibrary.service.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class BooksController {

    private BookRepository bookRepo;
    private BookService bookService;

    @Value("${upload.path}")
    private String uploadPath;

    public BooksController(BookRepository bookRepo, BookService service) {
        this.bookRepo = bookRepo;
        this.bookService = service;
    }

    @GetMapping("/")
    public String getStartPage() {
        return "greeting";
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        PageRequest last = PageRequest.of(0, 5, Sort.by("creationDate").descending());
        List<Book> books = bookService.getByPage(last);
        model.addAttribute("booksList", books);

        PageRequest popular = PageRequest.of(0, 3, Sort.by("mark").descending());
        List<Book> popularBooks = bookService.getByPage(popular);
        model.addAttribute("popular", popularBooks);
        return "booksList";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping(value = "download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> download(@PathVariable("id") Long id) throws IOException {
        return bookService.download(id);
    }
}
