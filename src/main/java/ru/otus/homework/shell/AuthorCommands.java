package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.service.AuthorService;

@ShellComponent
public class AuthorCommands {
    private final AuthorService service;

    public AuthorCommands(AuthorService service) {
        this.service = service;
    }

    @ShellMethod(key = {"ai", "aInsert"}, value = "Insert author. Arguments: author. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String insert(@ShellOption("Author") String authorName) {
        return service.saveAuthor(reformatString(authorName));
    }

    @ShellMethod(key = {"abn", "authorByName"}, value = "Get author by name. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getAuthorByName(@ShellOption("Name") String name) {
        return service.getAuthorByName(reformatString(name)).toString();
    }

    @ShellMethod(key = {"aga", "aGetAll"}, value = "Get all authors")
    public String getAll() {
        return service.getAll().toString();
    }

    @ShellMethod(key = {"au", "aUpdate"}, value = "Update author in repository. Arguments: oldAuthorName, " +
            "newAuthorName. Please, put comma instead of space in each argument or simply put the " +
            "arguments in quotes.")
    public String update(@ShellOption("OldAuthorName") String oldAuthorName,
                         @ShellOption("Name") String name) {
        return service.updateAuthor(reformatString(oldAuthorName), reformatString(name));
    }

    @ShellMethod(key = {"ad", "aDelete"}, value = "Delete author by name")
    public String deleteByName(@ShellOption("Name") String name) {
        return service.deleteAuthorByName(reformatString(name));
    }

    private String reformatString(String str) {
        return String.join(" ", str.split(","));
    }
}
