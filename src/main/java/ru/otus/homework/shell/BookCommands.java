package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Book;
import ru.otus.homework.service.BookInsertUpdateService;

@ShellComponent
public class BookCommands {
    private final BookDao bookDao;
    private final BookInsertUpdateService bookInsertUpdateService;

    public BookCommands(BookDao bookDao, BookInsertUpdateService bookInsertUpdateService) {
        this.bookDao = bookDao;
        this.bookInsertUpdateService = bookInsertUpdateService;
    }

    @ShellMethod(key = {"bi", "bInsert"}, value = "Insert book. Arguments: id, title, author, genre. " +
            "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("Id") long id,
                         @ShellOption("Title") String title,
                         @ShellOption("Author") String authorNameParameter,
                         @ShellOption("Genre") String genreNameParameter){
        bookInsertUpdateService.insertBook(id, title, authorNameParameter, genreNameParameter);
        return String.format("You successfully inserted a %s to repository",
                reformatString(title));
    }

    @ShellMethod(key = {"bbi", "bookById"}, value = "Get book by id")
    public String getBookById(@ShellOption("Id") long id){
        return bookDao.getBookById(id).toString();
    }

    @ShellMethod(key = {"bbt", "bookByTitle"}, value = "Get book by title. " +
            "Please, put comma instead of space in each argument")
    public String getBookByTitle(@ShellOption("Title") String title){
        return bookDao.getBookByTitle(reformatString(title)).toString();
    }

    @ShellMethod(key = {"bba", "bookByAuthor"}, value = "Get book by author. " +
            "Please, put comma instead of space in each argument")
    public String getBookByAuthor(@ShellOption("Author") String author){
        return bookDao.getBookByAuthor(reformatString(author)).toString();
    }

    @ShellMethod(key = {"bbg", "bookByGenre"}, value = "Get book by genre. " +
            "Please, put comma instead of space in each argument")
    public String getBookByGenre(@ShellOption("Genre") String genre){
        return bookDao.getBookByGenre(reformatString(genre)).toString();
    }

    @ShellMethod(key = {"bga", "bGetAll"}, value = "Get all books")
    public String getAll(){
        return bookDao.getAll().toString();
    }

    @ShellMethod(key = {"bu", "bUpdate"}, value = "Update book in repository. Arguments: id, title, author, genre. " +
            "Please, put comma instead of space in each argument")
    public String update(@ShellOption("Id") long id,
                         @ShellOption("Title") String title,
                         @ShellOption("Author") String authorNameParameter,
                         @ShellOption("Genre") String genreNameParameter){
        bookInsertUpdateService.updateBook(id, title, authorNameParameter, genreNameParameter);
        return String.format("%s was updated", reformatString(title));
    }

    @ShellMethod(key = {"bd", "bDelete"}, value = "Delete book by id")
    public String deleteById(@ShellOption("Id") long id){
        final Book book = bookDao.getBookById(id);
        bookDao.deleteById(id);
        return String.format("%s was deleted", book.getTitle());
    }

    private String reformatString(String str){
        return String.join(" ", str.split(","));
    }
}
