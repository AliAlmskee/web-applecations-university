package com.main.entity;

import com.main.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class Book extends BaseEntity {

    private String author;
    private String isbn;

    public Book() {
    }

    private Book(Builder builder) {
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

        public Book build() {
            return new Book(this);
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
