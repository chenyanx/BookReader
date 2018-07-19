package com.example.jeremychen.bookreader.Database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by jeremychen on 2018/7/7.
 */
@DatabaseTable(tableName = "Book.db")
public class DatabaseObject {

    @DatabaseField(id = true, columnName = "_id")
    private int id;

    @DatabaseField(columnName = "fileName")
    private String fileName;

    @DatabaseField(columnName = "Status")
    private boolean Download_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isDownload_status() {
        return Download_status;
    }

    public void setDownload_status(boolean download_status) {
        Download_status = download_status;
    }
}
