package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.service.CommentService;

@ShellComponent
public class CommentCommands {
    private final CommentService service;

    public CommentCommands(CommentService service) {
        this.service = service;
    }

    @ShellMethod(key = {"ci", "cInsert"}, value = "Insert comment. Arguments: book, comment. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String insert(@ShellOption("BookTitle") String bookTitle,
                         @ShellOption("Comment") String commentContent) {
        return service.saveComment(reformatString(bookTitle), reformatString(commentContent));
    }

    @ShellMethod(key = {"cbc", "cByContent", "commentByContent"}, value = "Get comment by content. Arguments: " +
            "commentContent. Please, put comma instead of space in each argument or simply put the arguments " +
            "in quotes.")
    public String getCommentByContent(@ShellOption("Content") String content) {
        return service.getCommentByContent(reformatString(content)).toString();
    }

    @ShellMethod(key = {"cbb", "cByBook", "commentByBook"}, value = "Get comment by book title. Arguments: " +
            "bookTitle. Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getCommentByBook(@ShellOption("Book title") String bookTitle) {
        return service.getCommentsByBook(reformatString(bookTitle)).toString();
    }

    @ShellMethod(key = {"cga", "cGetAll"}, value = "Get all comments")
    public String getAll() {
        return service.getAll().toString();
    }

    @ShellMethod(key = {"cu", "cUpdate"}, value = "Update comment in repository. Arguments: oldCommentContent, " +
            "newCommentContent. Please, put comma instead of space in each argument or simply put the arguments " +
            "in quotes.")
    public String update(@ShellOption("OldCommentContent") String oldCommentContent,
                         @ShellOption("Comment") String commentContent) {
        return service.updateComment(reformatString(oldCommentContent), reformatString(commentContent));
    }

    @ShellMethod(key = {"cd", "cDelete"}, value = "Delete comment by content. Arguments: commentContent. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String deleteByContent(@ShellOption("Content") String content) {
        return service.deleteByContent(reformatString(content));
    }

    private String reformatString(String str) {
        return String.join(" ", str.split(","));
    }
}
