package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.entity.Role;
import lib.iLibrary.entity.User;
import lib.iLibrary.service.BookService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Controller
public class BooksController {

    private BookService bookService;

    public BooksController(BookService service) {
        this.bookService = service;
    }

    @GetMapping("/books")
    public String getBooks(Model model, @AuthenticationPrincipal User user) {
        PageRequest last = PageRequest.of(0, 5, Sort.by("creationDate").descending());
        List<Book> books = bookService.getByPage(last);
        model.addAttribute("booksList", books);

        PageRequest popular = PageRequest.of(0, 3, Sort.by("mark").descending());
        List<Book> popularBooks = bookService.getByPage(popular);
        model.addAttribute("popular", popularBooks);

        boolean isAdmin = user.getRoles().contains(Role.ADMIN);
        model.addAttribute("admin", isAdmin);

        return "booksList";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id,
                             @AuthenticationPrincipal User user) {

        bookService.delete(id, user);
        return "redirect:/books";
    }

    @GetMapping(value = "download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> download(@PathVariable("id") Long id) throws IOException {
        return bookService.download(id);
    }
}
