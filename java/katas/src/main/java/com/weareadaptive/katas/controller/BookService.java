package com.weareadaptive.katas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookService {
  private final Map<Integer, Book> books = new HashMap<>();
  private int nextId;

  public Optional<Book> get(int id) {
    return Optional.ofNullable(books.get(id));
  }

  public Book create(String title, String summary, String isbn, String author) {
    var book = new Book(++nextId, title, summary, isbn, author);
    return books.put(book.getId(), book);
  }

  public Optional<Book> update(int id, String title, String summary, String isbn, String author) {
    var book = books.get(id);
    if (book == null) {
      return Optional.empty();
    }
    book.setIsbn(isbn);
    book.setSummary(summary);
    book.setTitle(title);
    book.setAuthor(author);
    return Optional.of(book);
  }

  public List<Book> getBooks() {
    return getBooks(null);
  }

  public List<Book> getBooks(String author) {
    var allBooks = books.values().stream();
    if (author != null) {
      allBooks = allBooks.filter(b -> author.equals(b.getAuthor()));
    }
    return allBooks.toList();
  }

  public boolean delete(int id) {
    return books.remove(id) != null;
  }


}
