package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Book;

@ShellComponent
public class BookCommands {
    private final BookDao bookDao;

    public BookCommands(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @ShellMethod(key = {"bi", "bInsert"}, value = "Insert book. Arguments: id, title, author, genre. " +
            "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("Id") long id,
                         @ShellOption("Title") String title,
                         @ShellOption("Author") String author,
                         @ShellOption("Genre") String genre){
        final Book book = new Book(id, String.join(" ", title.split(",")),
                String.join(" ", author.split(",")), String.join(" ", genre.split(",")));
        bookDao.insert(book);
        return String.format("You successfully inserted a %s to repository", String.join(" ", title.split(",")));
    }

    @ShellMethod(key = {"bbi", "bookById"}, value = "Get book by id")
    public String getBookById(@ShellOption("Id") long id){
        return bookDao.getBookById(id).toString();
    }

    @ShellMethod(key = {"bbt", "bookByTitle"}, value = "Get book by title. " +
            "Please, put comma instead of space in each argument")
    public Book getBookByTitle(@ShellOption("Title") String title){
        return bookDao.getBookByTitle(String.join(" ", title.split(",")));
    }

    @ShellMethod(key = {"bba", "bookByAuthor"}, value = "Get book by author. " +
            "Please, put comma instead of space in each argument")
    public Book getBookByAuthor(@ShellOption("Author") String author){
        return bookDao.getBookByAuthor(String.join(" ", author.split(",")));
    }

    @ShellMethod(key = {"bbg", "bookByGenre"}, value = "Get book by genre. " +
            "Please, put comma instead of space in each argument")
    public Book getBookByGenre(@ShellOption("Genre") String genre){
        return bookDao.getBookByGenre(String.join(" ", genre.split(",")));
    }

    @ShellMethod(key = {"bga", "bGetAll"}, value = "Get all books")
    public String getAll(){
        return bookDao.getAll().toString();
    }

    @ShellMethod(key = {"bu", "bUpdate"}, value = "Update book in repository. Arguments: id, title, author, genre. " +
            "Please, put comma instead of space in each argument")
    public String update(@ShellOption("Id") long id,
                         @ShellOption("Title") String title,
                         @ShellOption("Author") String author,
                         @ShellOption("Genre") String genre){
        final Book book = new Book(id, String.join(" ", title.split(",")),
                String.join(" ", author.split(",")), String.join(" ", genre.split(",")));
        bookDao.update(book);
        return String.format("%s was updated", book.getTitle());
    }

    @ShellMethod(key = {"bd", "bDelete"}, value = "Delete book by id")
    public String deleteById(@ShellOption("Id") long id){
        final Book book = bookDao.getBookById(id);
        bookDao.deleteById(id);
        return String.format("%s was deleted", book.getTitle());
    }
}
