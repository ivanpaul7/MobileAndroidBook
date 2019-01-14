package com.example.ivanp.book.vo;

import java.util.List;

public class Page {
    private int number;
    private List<Book> books;

//    public Page(int number, List<Book> books) {
//        this.number = number;
//        this.books = books;
//    }

    public int getNumber() {
        return number;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }



    @Override
    public String toString() {
        return "Page{" +
                "number=" + number +
                ", notes=" + books +
                '}';
    }
}
