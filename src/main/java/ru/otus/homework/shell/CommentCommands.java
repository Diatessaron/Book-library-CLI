package ru.otus.homework.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;

@ShellComponent
public class CommentCommands {
    private CommentRepository commentRepository;
    private BookRepository bookRepository;

    public CommentCommands(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @ShellMethod(key = {"ci", "cInsert"}, value = "Insert genre. Arguments: book id, comment. " +
                        "Please, put comma instead of space in each argument")
    public String insert(@ShellOption("BookId") long bookId,
                         @ShellOption("Comment") String commentContent) {
        final Comment comment = new Comment(0L, reformatString(commentContent));
        commentRepository.save(comment);

        final Book book = bookRepository.getBookById(bookId).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id"));
        book.getComments().add(comment);
        bookRepository.update(book);

        return "You successfully added a comment to " + book.getTitle();
    }

    @ShellMethod(key = {"cbi", "cById", "commentById"}, value = "Get comment by id")
    public String getCommentById(@ShellOption("Id") long id) {
        return commentRepository.getCommentById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id")).toString();
    }

    @ShellMethod(key = {"cbc", "cByContent", "commentByContent"}, value = "Get comment by content")
    public String getCommentByContent(@ShellOption("Content") String content) {
        return commentRepository.getCommentByContent(reformatString(content)).toString();
    }

    @ShellMethod(key = {"cga", "cGetAll"}, value = "Get all comments")
    public String getAll() {
        return commentRepository.getAll().toString();
    }

    @ShellMethod(key = {"cu", "cUpdate"}, value = "Update genre in repository. Arguments: bookId, commentId, " +
            "name. Please, put comma instead of space in each argument")
    public String update(@ShellOption("BookId") long bookId,
                         @ShellOption("CommentId to replace") long commentId,
                         @ShellOption("Content") String commentContent) {
        final Comment comment = new Comment(commentId, reformatString(commentContent));
        commentRepository.update(comment);

        final Book book = bookRepository.getBookById(bookId).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id"));
        book.getComments().removeIf(c -> c.getId() == commentId);
        book.getComments().add(comment);
        bookRepository.update(book);

        return book.getTitle() + " comment was updated";
    }

    @ShellMethod(key = {"cd", "cDelete"}, value = "Delete comment by id")
    public String deleteById(@ShellOption("Id") long id) {
        final Comment comment = commentRepository.getCommentById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect comment id"));

        final Book book = bookRepository.getBookByComment(comment.getContent());
        commentRepository.deleteById(id);

        return book.getTitle() + " comment was deleted";
    }

    private String reformatString(String str){
        return String.join(" ", str.split(","));
    }
}
