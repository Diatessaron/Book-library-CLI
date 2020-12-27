package ru.otus.homework.service;

public interface BookInsertUpdateService {
    void insertBook(long id, String title, String authorNameParameter, String genreNameParameter);
    void updateBook(long id, String title, String authorNamePArameter, String genreNameParameter);
}
