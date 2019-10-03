package lib.iLibrary.repository;

import lib.iLibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {

    List<Book> findByNameStartingWith(String name);

    @Query(value = "SELECT b FROM Book b " +
            "WHERE b.author LIKE ?1%")
    List<Book> getBooksByAuthor(String author);

}
