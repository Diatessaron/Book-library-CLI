package ru.otus.homework.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    @Field("content")
    private String content;
    @Field("book")
    private Book book;

    public Comment() {
    }

    public Comment(String content, String bookTitle) {
        this.content = content;
        this.book = new Book();
        this.book.setTitle(bookTitle);
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Book getBook() {
        return book;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBook(String bookTitle){
        this.book.setTitle(bookTitle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return content.equals(comment.content) && book.equals(comment.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, book);
    }

    @Override
    public String toString() {
        return "Comment '" + content +
                "' to book " + book.getTitle();
    }
}
