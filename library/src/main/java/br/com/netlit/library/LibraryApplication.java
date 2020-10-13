package br.com.netlit.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class LibraryApplication {

  public static void main(final String[] args) {
    SpringApplication.run(LibraryApplication.class, args);
  }
}
