package ru.otus.homework.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "comments")
public class Comment {
    @Field(name = "content")
    private String content;
    @Field(name = "bookTitle")
    private String bookTitle;

    public Comment() {
    }

    public Comment(String content, String bookTitle) {
        this.content = content;
        this.bookTitle = bookTitle;
    }

    public String getContent() {
        return content;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return content.equals(comment.content) && bookTitle.equals(comment.bookTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, bookTitle);
    }

    @Override
    public String toString() {
        return "Comment '" + content +
                "' to book " + bookTitle;
    }
}
