package com.example.jeremychen.bookreader.GsonObjects;

/**
 * Created by jeremychen on 2018/7/5.
 */
public class Book
{
    private String BookName;
    private String BookFile;

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getBookFile() {
        return BookFile;
    }

    public void setBookFile(String bookFile) {
        BookFile = bookFile;
    }
}
