package lib.iLibrary.RESTController;

import lib.iLibrary.entity.Book;
import lib.iLibrary.exceptions.RESTBookNotFoundException;
import lib.iLibrary.repository.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/books")
public class RESTBooksController {

    private BookRepository bookRepo;

    public RESTBooksController(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> all = bookRepo.findAll();
        if (all == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(all);
        }
    }

    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Book> getBooksByName(@PathVariable("name") String name) {
        List<Book> books = bookRepo.findByNameStartingWith(name);
        if (books.size() == 0) {
            throw new RESTBookNotFoundException("Book with name " + name + " not found");
        } else {
            return books;
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        Book savedBook = bookRepo.save(book);
        if (savedBook == null) {
            throw new RESTBookNotFoundException("Unable to create book");
        } else {
            return savedBook;
        }
    }

    @PutMapping(value = "/{id}")
    public Book correctBook(@PathVariable("id") Long id,
                            @RequestBody Book correctedBook) {

        Book book = bookRepo.findById(id).orElseThrow(RESTBookNotFoundException::new);
        BeanUtils.copyProperties(correctedBook, book, "id", "creationDate");
        Book savedBook = bookRepo.save(book);
        if (savedBook == null) {
            throw new RESTBookNotFoundException("Unable to edit book");
        } else {
            return savedBook;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        bookRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
