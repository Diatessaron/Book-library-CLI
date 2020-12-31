package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.service.BookInsertUpdateService;

@ShellComponent
public class BookCommands {
    private final BookRepository bookRepository;
    private final BookInsertUpdateService bookInsertUpdateService;

    public BookCommands(BookRepository bookRepository, BookInsertUpdateService bookInsertUpdateService) {
        this.bookRepository = bookRepository;
        this.bookInsertUpdateService = bookInsertUpdateService;
    }

    @ShellMethod(key = {"bi", "bInsert"}, value = "Insert book. Arguments: title, author, genre. " +
            "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("Title") String title,
                         @ShellOption("Author") String authorNameParameter,
                         @ShellOption("Genre") String genreNameParameter){
        bookInsertUpdateService.saveBook(reformatString(title), reformatString(authorNameParameter),
                reformatString(genreNameParameter));
        return String.format("You successfully inserted a %s to repository",
                reformatString(title));
    }

    @ShellMethod(key = {"bbi", "bookById"}, value = "Get book by id")
    public String getBookById(@ShellOption("Id") long id){
        return bookRepository.getBookById(id).toString();
    }

    @ShellMethod(key = {"bbt", "bookByTitle"}, value = "Get book by title. " +
            "Please, put comma instead of space in each argument")
    public String getBookByTitle(@ShellOption("Title") String title){
        return bookRepository.getBookByTitle(reformatString(title)).toString();
    }

    @ShellMethod(key = {"bba", "bookByAuthor"}, value = "Get book by author. " +
            "Please, put comma instead of space in each argument")
    public String getBookByAuthor(@ShellOption("Author") String author){
        return bookRepository.getBookByAuthor(reformatString(author)).toString();
    }

    @ShellMethod(key = {"bbg", "bookByGenre"}, value = "Get book by genre. " +
            "Please, put comma instead of space in each argument")
    public String getBookByGenre(@ShellOption("Genre") String genre){
        return bookRepository.getBookByGenre(reformatString(genre)).toString();
    }

    @ShellMethod(key = {"bga", "bGetAll"}, value = "Get all books")
    public String getAll(){
        return bookRepository.getAll().toString();
    }

    @ShellMethod(key = {"bu", "bUpdate"}, value = "Update book in repository. Arguments: id, title, author, genre. " +
            "Please, put comma instead of space in each argument")
    public String update(@ShellOption("Id") long id,
                         @ShellOption("Title") String title,
                         @ShellOption("Author") String authorNameParameter,
                         @ShellOption("Genre") String genreNameParameter){
        bookInsertUpdateService.updateBook(id, reformatString(title),
                reformatString(authorNameParameter), reformatString(genreNameParameter));
        return String.format("%s was updated", reformatString(title));
    }

    @ShellMethod(key = {"bd", "bDelete"}, value = "Delete book by id")
    public String deleteById(@ShellOption("Id") long id){
        final Book book = bookRepository.getBookById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect id"));
        bookRepository.deleteById(id);
        return String.format("%s was deleted", book.getTitle());
    }

    private String reformatString(String str){
        return String.join(" ", str.split(","));
    }
}
