package ru.otus.homework.shell;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Author;

@ShellComponent
public class AuthorCommands {
    private final AuthorDao authorDao;
    private final BookDao bookDao;

    public AuthorCommands(AuthorDao authorDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
    }

    @ShellMethod(key = {"ai", "aInsert"}, value = "Insert author. Arguments: id, author. " +
            "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("Id") long id,
                         @ShellOption("Author") String authorName){
        final Author author = new Author(id, String.join(" ", authorName.split(",")));
        authorDao.insert(author);
        return String.format("You successfully inserted a %s to repository", author.getName());
    }

    @ShellMethod(key = {"abi", "authorById"}, value = "Get author by id")
    public String getAuthorById(@ShellOption("Id") long id){
        return authorDao.getAuthorById(id).toString();
    }

    @ShellMethod(key = {"abn", "authorByName"}, value = "Get author by name. " +
            "Please, put comma instead of space in each argument")
    public String getAuthorByName(@ShellOption("Name") String name){
        return authorDao.getAuthorByName(String.join(" ", name.split(","))).toString();
    }

    @ShellMethod(key = {"aga", "aGetAll"}, value = "Get all authors")
    public String getAll(){
        return authorDao.getAll().toString();
    }

    @ShellMethod(key = {"au", "aUpdate"}, value = "Update author in repository. Arguments: id, author. " +
            "Please, put comma instead of space in each argument")
    public String update(@ShellOption("Id") long id,
                         @ShellOption("Name") String name){
        final Author author = new Author(id, String.join(" ", name.split(",")));
        authorDao.update(author);
        return String.format("%s was updated", author.getName());
    }

    @ShellMethod(key = {"ad", "aDelete"}, value = "Delete author by id")
    public String deleteById(@ShellOption("Id") long id){
        final Author author = authorDao.getAuthorById(id);
        try{
            bookDao.deleteById(bookDao.getBookByAuthor(author.getName()).getId());
        } catch (EmptyResultDataAccessException e){
            e.printStackTrace();
        }
        authorDao.deleteById(id);

        return String.format("%s was deleted", author.getName());
    }
}
