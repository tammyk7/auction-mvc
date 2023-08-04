package com.weareadaptive.katas.controller;

public class Book {
  private final int id;
  private String title;
  private String summary;
  private String isbn;
  private String author;

  public Book(int id, String title, String summary, String isbn, String author) {
    this.id = id;
    this.title = title;
    this.summary = summary;
    this.isbn = isbn;
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public String getSummary() {
    return summary;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public int getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }
}
