package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.domain.Book;
import ru.otus.homework.service.BookService;

@ShellComponent
public class BookCommands {
    private final BookService bookService;

    public BookCommands(BookService bookService) {
        this.bookService = bookService;
    }

    @ShellMethod(key = {"bi", "bInsert"}, value = "Insert book. Arguments: title, author, genre. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String insert(@ShellOption("Title") String title,
                         @ShellOption("Author") String authorNameParameter,
                         @ShellOption("Genre") String genreNameParameter) {
        bookService.saveBook(reformatString(title), reformatString(authorNameParameter),
                reformatString(genreNameParameter));
        return String.format("You successfully inserted a %s to repository",
                reformatString(title));
    }

    @ShellMethod(key = {"bbt", "bookByTitle"}, value = "Get book by title. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getBookByTitle(@ShellOption("Title") String title) {
        return bookService.getBookByTitle(reformatString(title)).toString();
    }

    @ShellMethod(key = {"bba", "bookByAuthor"}, value = "Get book by author. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getBookByAuthor(@ShellOption("Author") String author) {
        return bookService.getBookByAuthor(reformatString(author)).toString();
    }

    @ShellMethod(key = {"bbg", "bookByGenre"}, value = "Get book by genre. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getBookByGenre(@ShellOption("Genre") String genre) {
        return bookService.getBookByGenre(reformatString(genre)).toString();
    }

    @ShellMethod(key = {"bbc", "bByComment", "bookByComment"}, value = "Get book by comment. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getBookByComment(@ShellOption("Comment") String commentContent) {
        return bookService.getBookByComment(reformatString(commentContent)).toString();
    }

    @ShellMethod(key = {"bga", "bGetAll"}, value = "Get all books")
    public String getAll() {
        return bookService.getAll().toString();
    }

    @ShellMethod(key = {"bu", "bUpdate"}, value = "Update book in repository. Arguments: oldBookTitle, title, " +
            "author, genre. Please, put comma instead of space in each argument or simply put the " +
            "arguments in quotes.")
    public String update(@ShellOption("OldBookTitle") String oldBookTitle,
                         @ShellOption("Title") String title,
                         @ShellOption("Author") String authorNameParameter,
                         @ShellOption("Genre") String genreNameParameter) {
        bookService.updateBook(reformatString(oldBookTitle), reformatString(title),
                reformatString(authorNameParameter), reformatString(genreNameParameter));
        return String.format("%s was updated", reformatString(title));
    }

    @ShellMethod(key = {"bd", "bDelete"}, value = "Delete book by name")
    public String deleteByName(@ShellOption("Name") String name) {
        final Book book = bookService.getBookByTitle(reformatString(name));
        bookService.deleteBookByTitle(reformatString(name));
        return String.format("%s was deleted", book.getTitle());
    }

    private String reformatString(String str) {
        return String.join(" ", str.split(","));
    }
}
