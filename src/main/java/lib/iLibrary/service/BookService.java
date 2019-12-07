package lib.iLibrary.service;

import lib.iLibrary.entity.Book;
import lib.iLibrary.entity.User;
import lib.iLibrary.exceptions.NoCurrentBookException;
import lib.iLibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    @Value("${upload.path}")
    private String uploadPath;

    private BookRepository bookRepo;

    public BookService(BookRepository repo) {
        this.bookRepo = repo;
    }

    public Book getById(Long id) {
        Optional<Book> optionalBook = bookRepo.findById(id);
        return optionalBook.orElseThrow(NoCurrentBookException::new);
    }

    public List<Book> getByPage(Pageable page) {
        return bookRepo.findAll(page).getContent();
    }

    public List<Book> getByName(String name) {
        return bookRepo.findByNameStartingWith(name);
    }

    public List<Book> getByAuthor(String author) {
        return bookRepo.getBooksByAuthor(author);
    }

    @Transactional
    public void delete(Long id, User user) {

        Book book = getById(id);
        user.getBookList().remove(book);

        String fileName = book.getFileName();
        File file = new File(uploadPath + File.separator + fileName);
        file.delete();
        bookRepo.delete(book);
    }

    @Transactional
    public void deleteFile(Book book) {
        String fileName = book.getFileName();

        File file = new File(uploadPath + "/" + fileName);
        file.delete();
        book.setFileName(null);
        bookRepo.save(book);
    }

    @Transactional
    public Book addFileAndSave(Book book, MultipartFile file) {

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File upPath = new File(uploadPath);
            if (!upPath.exists()) {
                upPath.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            try {
                file.transferTo(upPath.toPath().resolve(resultFilename));
            } catch (IOException e) {
                System.out.println("Some problems with uploading file");
            }
            book.setFileName(resultFilename);
        }
        return bookRepo.save(book);
    }

    @Transactional
    public ResponseEntity<ByteArrayResource> download(Long id) throws IOException {
        Optional<Book> optionalBook = bookRepo.findById(id);
        if (optionalBook.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Book book = optionalBook.get();
        String fileName = book.getFileName();
        String name = fileName.replaceFirst(".+?\\.", "");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "filename=" + name);

        ByteArrayResource resource =
                new ByteArrayResource(Files.readAllBytes(Paths.get(uploadPath + "/" + fileName)));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
