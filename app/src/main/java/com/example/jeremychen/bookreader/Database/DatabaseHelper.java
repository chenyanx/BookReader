package com.example.jeremychen.bookreader.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by jeremychen on 2018/7/7.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static DatabaseHelper helper = null;
    public DatabaseHelper(Context context) {
        super(context, "BookReader.db", null, 100087);
    }

    public static DatabaseHelper getInstance(Context context)
    {
        if(helper == null)
        {
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DatabaseObject.class);
            TableUtils.createTable(connectionSource, DatabaseProgressObject.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,DatabaseObject.class,true);
            TableUtils.dropTable(connectionSource,DatabaseProgressObject.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
