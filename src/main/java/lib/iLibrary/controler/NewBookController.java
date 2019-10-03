package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/newBook")
public class NewBookController {

    private BookRepo bookRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public NewBookController(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping
    public String newBookPage(Model model) {
        model.addAttribute("newBook", new Book());
        return "newBook";
    }

    @PostMapping
    public String addBook(@ModelAttribute Book newBook, @RequestParam("file") MultipartFile file) throws IOException {

        if (file != null && !file.getOriginalFilename().isEmpty()) {

            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(path.resolve(resultFilename));
            newBook.setFileName(resultFilename);
        }
        bookRepo.save(newBook);
        return "redirect:/books";
    }
}
