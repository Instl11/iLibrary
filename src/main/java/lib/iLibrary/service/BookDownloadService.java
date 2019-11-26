package lib.iLibrary.service;

import lib.iLibrary.entity.Book;
import lib.iLibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class BookDownloadService {

    @Value("${upload.path}")
    private String uploadPath;

    private BookRepository bookRepo;

    public BookDownloadService(BookRepository repo) {
        this.bookRepo = repo;
    }

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
