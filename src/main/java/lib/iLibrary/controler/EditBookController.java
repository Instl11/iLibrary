package lib.iLibrary.controler;

import lib.iLibrary.entity.Book;
import lib.iLibrary.exceptions.NoCurrentBookException;
import lib.iLibrary.repository.BookRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("edit")
public class EditBookController {

    private BookRepo bookRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public EditBookController(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping("/{id}")
    public String editBookPage(@PathVariable("id") Long id, Model model){
        try {
            Book book = bookRepo.findById(id).orElseThrow(NoCurrentBookException::new);
            model.addAttribute("editedBook", book);
        }catch (NoCurrentBookException e){
            return "noCurrentBook";
        }
        return "editForm";
    }

    @PostMapping("/{id}")
    public String editBook(@ModelAttribute Book editedBook, @RequestParam("file") MultipartFile file) throws IOException {

        Book book = bookRepo.findById(editedBook.getId()).get();
        BeanUtils.copyProperties(editedBook, book, "id", "creationDate", "fileName");

        if (file != null && !file.getOriginalFilename().isEmpty()){
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            book.setFileName(resultFilename);
        }
        bookRepo.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") Long id){

        Book book = bookRepo.findById(id).get();
        String fileName = book.getFileName();

        File file = new File(uploadPath + "/" + fileName);

        file.delete();

        book.setFileName(null);
        bookRepo.save(book);

        return "redirect:/edit/" + id;
    }
}
