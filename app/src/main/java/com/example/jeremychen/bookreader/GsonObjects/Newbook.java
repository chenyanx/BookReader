package com.example.jeremychen.bookreader.GsonObjects;

import java.util.List;

/**
 * Created by jeremychen on 2018/7/5.
 */

public class Newbook {
    public int status;
    public String msg;
    public List<BookData> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BookData> getData() {
        return data;
    }

    public void setData(List<BookData> data) {
        this.data = data;
    }

    public class BookData
    {
        String bookname;
        String bookfile;

        public String getBookname() {
            return bookname;
        }

        public void setBookname(String bookname) {
            this.bookname = bookname;
        }

        public String getBookfile() {
            return bookfile;
        }

        public void setBookfile(String bookfile) {
            this.bookfile = bookfile;
        }
    }
}

