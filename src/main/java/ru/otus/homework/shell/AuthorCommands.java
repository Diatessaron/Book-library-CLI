package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.domain.Author;
import ru.otus.homework.repository.AuthorRepository;

@ShellComponent
public class AuthorCommands {
    private final AuthorRepository authorRepository;

    public AuthorCommands(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @ShellMethod(key = {"ai", "aInsert"}, value = "Insert author. Arguments: id, author. " +
            "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("Id") long id,
                         @ShellOption("Author") String authorName){
        final Author author = new Author(id, String.join(" ", authorName.split(",")));
        authorRepository.save(author);
        return String.format("You successfully saved a %s to repository", author.getName());
    }

    @ShellMethod(key = {"abi", "authorById"}, value = "Get author by id")
    public String getAuthorById(@ShellOption("Id") long id){
        return authorRepository.getAuthorById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id")).toString();
    }

    @ShellMethod(key = {"abn", "authorByName"}, value = "Get author by name. " +
            "Please, put comma instead of space in each argument")
    public String getAuthorByName(@ShellOption("Name") String name){
        return authorRepository.getAuthorByName(String.join(" ", name.split(","))).toString();
    }

    @ShellMethod(key = {"aga", "aGetAll"}, value = "Get all authors")
    public String getAll(){
        return authorRepository.getAll().toString();
    }

    @ShellMethod(key = {"au", "aUpdate"}, value = "Update author in repository. Arguments: id, author. " +
            "Please, put comma instead of space in each argument")
    public String update(@ShellOption("Id") long id,
                         @ShellOption("Name") String name){
        final Author author = new Author(id, String.join(" ", name.split(",")));
        authorRepository.update(author);
        return String.format("%s was updated", author.getName());
    }

    @ShellMethod(key = {"ad", "aDelete"}, value = "Delete author by id")
    public String deleteById(@ShellOption("Id") long id){
        final Author author = authorRepository.getAuthorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id"));
        authorRepository.deleteById(id);

        return String.format("%s was deleted", author.getName());
    }
}
