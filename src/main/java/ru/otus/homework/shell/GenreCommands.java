package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Genre;

@ShellComponent
public class GenreCommands {
    private final GenreDao genreDao;

    public GenreCommands(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @ShellMethod(key = {"gi", "gInsert"}, value = "Insert genre. Arguments: id, title. " +
            "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("Id") long id,
                         @ShellOption("Title") String title){
        final Genre genre = new Genre(id, String.join(" ", title.split(",")));
        genreDao.insert(genre);
        return String.format("You successfully inserted a %s to repository", genre.getTitle());
    }

    @ShellMethod(key = {"gbi", "genreById"}, value = "Get genre by id")
    public String getGenreById(@ShellOption("Id") long id){
        return genreDao.getGenreById(id).toString();
    }

    @ShellMethod(key = {"gbt", "genreByTitle"}, value = "Get genre by title. " +
            "Please, put comma instead of space in each argument")
    public String getGenreByTitle(@ShellOption("Title") String title){
        return genreDao.getGenreByTitle(String.join(" ", title.split(","))).toString();
    }

    @ShellMethod(key = {"gga", "gGetAll"}, value = "Get all genres")
    public String getAll(){
        return genreDao.getAll().toString();
    }

    @ShellMethod(key = {"gu", "gUpdate"}, value = "Update genre in repository. Arguments: id, title. " +
            "Please, put comma instead of space in each argument")
    public String update(@ShellOption("Id") long id,
                         @ShellOption("Title") String title){
        final Genre genre = new Genre(id, String.join(" ", title.split(",")));
        genreDao.update(genre);
        return String.format("%s was updated", genre.getTitle());
    }

    @ShellMethod(key = {"gd", "gDelete"}, value = "Delete genre by id")
    public String deleteById(@ShellOption("Id") long id){
        final Genre genre = genreDao.getGenreById(id);
        genreDao.deleteById(id);
        return String.format("%s was deleted", genre.getTitle());
    }
}
