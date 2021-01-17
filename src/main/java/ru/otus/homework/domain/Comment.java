package ru.otus.homework.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedEntityGraph(name = "Comment.book",
        attributeNodes = @NamedAttributeNode("book"))
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "content")
    private String content;
    @ManyToOne(targetEntity = Book.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    public Comment() {
    }

    public Comment(long id, String content, Book book) {
        this.id = id;
        this.content = content;
        this.book = book;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Book getBook() {
        return book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && content.equals(comment.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }

    @Override
    public String toString() {
        return "Comment '" + content +
                "' to book " + book.getTitle();
    }
}
