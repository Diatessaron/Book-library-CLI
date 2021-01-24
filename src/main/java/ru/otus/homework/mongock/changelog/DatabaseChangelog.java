package ru.otus.homework.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.repository.GenreRepository;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "001", id = "dropDb", runAlways = true, author = "Diatessaron")
    public void dropDb(MongoDatabase db){
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthor", runAlways = true, author = "Diatessaron")
    public void insertAuthor(AuthorRepository repository){
        repository.save(new Author("James Joyce"));
    }

    @ChangeSet(order = "003", id = "insertGenre", runAlways = true, author = "Diatessaron")
    public void insertGenre(GenreRepository repository){
        repository.save(new Genre("Modernist novel"));
    }

    @ChangeSet(order = "004", id = "insertBook", runAlways = true, author = "Diatessaron")
    public void insertBook(BookRepository repository){
        repository.save(new Book("Ulysses", new Author("James Joyce"),
                new Genre("Modernist novel")));
    }

    @ChangeSet(order = "005", id = "insertComment", runAlways = true, author = "Diatessaron")
    public void insertComment(CommentRepository repository){
        repository.save(new Comment("Published in 1922", "Ulysses"));
    }
}
