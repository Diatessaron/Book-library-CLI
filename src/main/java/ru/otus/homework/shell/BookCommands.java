package ru.otus.homework.shell;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

@ShellComponent
public class BookCommands {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookCommands(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @ShellMethod(key = {"bi", "bInsert"}, value = "Insert book. Arguments: id, title, author, genre. " +
            "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("Id") long id,
                         @ShellOption("Title") String title,
                         @ShellOption("Author") String authorNameParameter,
                         @ShellOption("Genre") String genreNameParameter){
        final String authorName = String.join(" ", authorNameParameter.split(","));
        Author author;
        try{
            author = authorDao.getAuthorByName(authorName);
        } catch (EmptyResultDataAccessException e){
            author = new Author(authorDao.count()+1L, authorName);
            authorDao.insert(author);
        }

        final String genreName = String.join(" ", genreNameParameter.split(","));
        Genre genre;
        try{
            genre = genreDao.getGenreByName(genreName);
        } catch (EmptyResultDataAccessException e){
            genre = new Genre(genreDao.count()+1L, genreName);
            genreDao.insert(genre);
        }

        final Book book = new Book(id, String.join(" ", title.split(",")), author, genre);
        bookDao.insert(book);
        return String.format("You successfully inserted a %s to repository", String.join(" ", title.split(",")));
    }

    @ShellMethod(key = {"bbi", "bookById"}, value = "Get book by id")
    public String getBookById(@ShellOption("Id") long id){
        return bookDao.getBookById(id).toString();
    }

    @ShellMethod(key = {"bbt", "bookByTitle"}, value = "Get book by title. " +
            "Please, put comma instead of space in each argument")
    public String getBookByTitle(@ShellOption("Title") String title){
        return bookDao.getBookByTitle(String.join(" ", title.split(","))).toString();
    }

    @ShellMethod(key = {"bba", "bookByAuthor"}, value = "Get book by author. " +
            "Please, put comma instead of space in each argument")
    public String getBookByAuthor(@ShellOption("Author") String author){
        return bookDao.getBookByAuthor(String.join(" ", author.split(","))).toString();
    }

    @ShellMethod(key = {"bbg", "bookByGenre"}, value = "Get book by genre. " +
            "Please, put comma instead of space in each argument")
    public String getBookByGenre(@ShellOption("Genre") String genre){
        return bookDao.getBookByGenre(String.join(" ", genre.split(","))).toString();
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
        final String authorName = String.join(" ", authorNameParameter.split(","));
        Author author;
        try{
            author = authorDao.getAuthorByName(authorName);
        } catch (EmptyResultDataAccessException e){
            author = new Author(authorDao.count()+1L, authorName);
            authorDao.insert(author);
        }

        final String genreName = String.join(" ", genreNameParameter.split(","));
        Genre genre;
        try{
            genre = genreDao.getGenreByName(genreName);
        } catch (EmptyResultDataAccessException e){
            genre = new Genre(genreDao.count()+1L, genreName);
            genreDao.insert(genre);
        }

        final Book book = new Book(id, String.join(" ", title.split(",")), author, genre);
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
