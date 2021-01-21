package ru.otus.homework.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "comments")
public class Comment {
    @Field(name = "content")
    private String content;
    private Book book;

    public Comment() {
    }

    public Comment(String content, Book book) {
        this.content = content;
        this.book = book;
    }

    public String getContent() {
        return content;
    }

    public Book getBook() {
        return book;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBook(Book book) {
        this.book = book;
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
