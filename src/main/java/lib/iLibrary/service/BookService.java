package lib.iLibrary.service;

import lib.iLibrary.entity.Book;
import lib.iLibrary.repository.BookRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class BookService {

    private BookRepo repo;

    @Value("${upload.path}")
    private String uploadPath;

    public BookService(BookRepo repo) {
        this.repo = repo;
    }

    public ResponseEntity<ByteArrayResource> downloadFile(Book book) throws IOException {
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
