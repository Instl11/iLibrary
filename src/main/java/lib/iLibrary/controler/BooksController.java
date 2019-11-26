package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.exceptions.NoCurrentBookException;
import lib.iLibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class BooksController {

    private BookRepository bookRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public BooksController(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping("/")
    public String getStartPage() {
        return "greeting";
    }

    @GetMapping("/books")
    public String getBooks(Model model) {

        PageRequest last = PageRequest.of(0, 5, Sort.by("creationDate").descending());
        List<Book> books = bookRepo.findAll(last).getContent();
        model.addAttribute("booksList", books);

        PageRequest popular = PageRequest.of(0, 3, Sort.by("mark").descending());
        List<Book> popularBooks = bookRepo.findAll(popular).getContent();
        model.addAttribute("popular", popularBooks);
        return "booksList";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {

        Book book;
        try {
            book = bookRepo.findById(id).orElseThrow(NoCurrentBookException::new);
        } catch (NoCurrentBookException e) {
            return "noCurrentBook";
        }
        String fileName = book.getFileName();
        File file = new File(uploadPath + "/" + fileName);
        file.delete();

        bookRepo.delete(book);
        return "redirect:/books";
    }

    @GetMapping(value = "download/{id}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> download(@PathVariable("id") Book book) throws IOException {

        String fileName = book.getFileName();
        Path path = Paths.get(uploadPath).resolve(fileName);

        String name = fileName.replaceFirst(".+?\\.", "");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "filename=" + name);

        ByteArrayResource resource =
                new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(path.toFile().length())
                .body(resource);
    }
}
