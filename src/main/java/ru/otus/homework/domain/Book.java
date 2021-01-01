package ru.otus.homework.domain;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private String title;
    @ManyToOne(targetEntity = Author.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToOne(targetEntity = Genre.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id")
    private Genre genre;
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 5)
    @OneToMany(fetch = FetchType.EAGER, targetEntity = Comment.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private List<Comment> comments;

    public Book() {
    }

    public Book(long id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        comments = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public List<Comment> getComments(){
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && title.equals(book.title) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre) && comments.equals(book.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, genre, comments);
    }

    @Override
    public String toString() {
        final String commentsString = comments.stream().map(Comment::toString).reduce
                ("", (partialString, element) -> partialString + element + '\n');

        return "Title: " + title + '\n' +
                "Author: " + author.getName() + '\n' +
                "Genre: " + genre + '\n' +
                "Comments: " + commentsString;
    }
}
