package lib.iLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

@SpringBootApplication
public class ILibraryApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ILibraryApplication.class, args);
	}

}
