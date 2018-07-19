package com.example.jeremychen.bookreader.Database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by jeremychen on 2018/7/7.
 */
@DatabaseTable(tableName = "Progress.db")
public class DatabaseProgressObject {
    @DatabaseField(id = true, columnName = "_id")
    private int id;

    @DatabaseField(columnName = "bookName")
    private String bookName;

    @DatabaseField(columnName = "progress")
    private int progress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
