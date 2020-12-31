package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.GenreRepository;

@ShellComponent
public class GenreCommands {
    private final GenreRepository genreRepository;

    public GenreCommands(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @ShellMethod(key = {"gi", "gInsert"}, value = "Insert genre. Arguments: id, title. " +
            "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("Id") long id,
                         @ShellOption("Title") String title){
        final Genre genre = new Genre(id, String.join(" ", title.split(",")));
        genreRepository.save(genre);
        return String.format("You successfully saved a %s to repository", genre.getName());
    }

    @ShellMethod(key = {"gbi", "genreById"}, value = "Get genre by id")
    public String getGenreById(@ShellOption("Id") long id){
        return genreRepository.getGenreById(id).toString();
    }

    @ShellMethod(key = {"gbt", "genreByTitle"}, value = "Get genre by title. " +
            "Please, put comma instead of space in each argument")
    public String getGenreByTitle(@ShellOption("Title") String title){
        return genreRepository.getGenreByName(String.join(" ", title.split(","))).toString();
    }

    @ShellMethod(key = {"gga", "gGetAll"}, value = "Get all genres")
    public String getAll(){
        return genreRepository.getAll().toString();
    }

    @ShellMethod(key = {"gu", "gUpdate"}, value = "Update genre in repository. Arguments: id, title. " +
            "Please, put comma instead of space in each argument")
    public String update(@ShellOption("Id") long id,
                         @ShellOption("Title") String title){
        final Genre genre = new Genre(id, String.join(" ", title.split(",")));
        genreRepository.update(genre);
        return String.format("%s was updated", genre.getName());
    }

    @ShellMethod(key = {"gd", "gDelete"}, value = "Delete genre by id")
    public String deleteById(@ShellOption("Id") long id){
        final Genre genre = genreRepository.getGenreById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id"));
        genreRepository.deleteById(id);

        return String.format("%s was deleted", genre.getName());
    }
}
