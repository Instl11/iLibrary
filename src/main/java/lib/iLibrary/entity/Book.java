package lib.iLibrary.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    Timestamp creationDate;

    @NotBlank(message = "Please type book's name")
    private String name;

    @NotBlank(message = "Please type book's author")
    private String author;

    @Pattern(regexp = "[1-9]0?", message = "Please input mark from 1 to 10")
    private String mark;

    private String fileName;

}
