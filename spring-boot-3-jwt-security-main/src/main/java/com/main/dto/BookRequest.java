package com.main.dto;

public class BookRequest {
    private String author;
    private String isbn;

    public BookRequest() {
    }

    private BookRequest(Builder builder) {
        this.author = builder.author;
        this.isbn = builder.isbn;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String author;
        private String isbn;

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookRequest build() {
            return new BookRequest(this);
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
