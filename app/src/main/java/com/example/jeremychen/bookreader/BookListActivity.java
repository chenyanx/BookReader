package com.example.jeremychen.bookreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.jeremychen.bookreader.Adapter.Myadapter;
import com.example.jeremychen.bookreader.GsonObjects.Newbook;
import com.example.jeremychen.bookreader.Loader.MyLoader;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final String DOWNLOAD_URL = "http://www.imooc.com/api/teacher?type=10";
    private ListView listview;
    private List<Newbook.BookData> result_data = new ArrayList<>();
    private static final String TAG = "BookListActivity";
    private Myadapter myadapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        setTitle("BookList");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载，请稍后");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        initUI();
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(0, null, this);

        myadapter = new Myadapter(result_data, this);

        listview.setAdapter(myadapter);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void initUI() {
        listview = (ListView) findViewById(R.id.list_view_book_list);
    }

    public static void start(Context context)
    {
        Intent intent = new Intent(context, BookListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new MyLoader(this, DOWNLOAD_URL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        //解析并且设置数据
        Gson gson = new Gson();
        Newbook newbook = gson.fromJson(data, Newbook.class);
        if(result_data!= null)
        {
            result_data.clear();
        }
        result_data.addAll(newbook.getData());
        myadapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
