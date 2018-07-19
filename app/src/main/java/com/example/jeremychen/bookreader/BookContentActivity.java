package com.example.jeremychen.bookreader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jeremychen.bookreader.Adapter.RecyclerViewAdapter;
import com.example.jeremychen.bookreader.BookView.BookPageBezierHelper;
import com.example.jeremychen.bookreader.BookView.BookPageView;
import com.example.jeremychen.bookreader.Database.DatabaseHelper;
import com.example.jeremychen.bookreader.Database.DatabaseProgressObject;
import com.j256.ormlite.dao.Dao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookContentActivity extends AppCompatActivity {

    private BookPageView bookPageView;
    private TextView textView;
    private BookPageBezierHelper bookPageBezierHelper;
    private View view;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private final static int MEDIA_CODE = 1001;
    private int currentProgress = 0;
    private Bitmap currentBitmap;
    private Bitmap nextPageBitmap;
    private int width;
    private int height;
    private Dao<DatabaseProgressObject, Integer> dao;
    private String fileName;
    private int position;
    private boolean changedBG;
    private Bitmap ChangedBitmap;
    private TextToSpeech mTTs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置使得屏幕变为全屏状态
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_book_content);
        initUI();
        // --------------- 初始化DAO ------------------//
        try {
            dao = DatabaseHelper.getInstance(BookContentActivity.this).getDao(DatabaseProgressObject.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // ------------------ 初始化dao 用于纪录progress ------------//

        //get Size（获取屏幕的大小）
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        OpenBookByProgress(currentProgress, null);

        try {
            if(dao.queryForEq("bookName", fileName).size() == 0){
                DatabaseProgressObject object = new DatabaseProgressObject();
                object.setProgress(currentProgress);
                object.setBookName(fileName);
                object.setId(position);
                dao.createOrUpdate(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //---------------------RecyclerView-----------------------//
        //set recyclerView in the setting options:

        //setting the LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(this, getData());

        //SharedPreferences sharedPreferences = Activity.;

        adapter.setOnClickListener(new RecyclerViewAdapter.OnClickListener() {
            @Override
            public void OnClick(int position) {
                switch (position)
                {
                    case 0:
                        DatabaseProgressObject object = new DatabaseProgressObject();
                        object.setBookName(fileName);
                        object.setProgress(currentProgress);
                        object.setId(position);
                        try {
                            dao.createOrUpdate(object);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(BookContentActivity.this, "书签设置成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        try {
                            List<DatabaseProgressObject> list = dao.queryForEq("bookName", fileName);
                            int progress = list.get(0).getProgress();
                            OpenBookByProgress(progress, null);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, MEDIA_CODE);
                        break;
                    case 3:
                        if(mTTs == null) {
                            mTTs = new TextToSpeech(BookContentActivity.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    // TODO: 2018/7/15 语音朗读 
                                }
                            });
                        }
                        else{
                            if(mTTs.isSpeaking())
                            {
                                mTTs.stop();
                            }
                        }
                        break;
                    case 4:
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initUI() {
        bookPageView = (BookPageView) findViewById(R.id.book_page_view);
        textView = (TextView) findViewById(R.id.text_view_progress);
        view = findViewById(R.id.view_settings);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MEDIA_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            changedBG = true;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumn,null,null,null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumn[0]);
            String ImagePath = c.getString(columnIndex);
            Bitmap bitmap = BitmapFactory.decodeFile(ImagePath);
            ChangedBitmap = bitmap;
            OpenBookByProgress(currentProgress, ChangedBitmap);
            // TODO: 2018/7/7 set new background!
            c.close();
        }
    }


    //----------------------------- 初始化所有关于BookPageView -----------------------//
    private void OpenBookByProgress(int progress, Bitmap bitmap){
        bookPageBezierHelper = new BookPageBezierHelper(width, height, progress);
        bookPageView.setBookPageBezierHelper(bookPageBezierHelper);
        if(changedBG == false) {
            bookPageBezierHelper.setBackground(this, R.drawable.book_bg);
        }
        else {
            bookPageBezierHelper.setBackground(ChangedBitmap);
        }

        //----------------纪录progress的变化-------------------//
        bookPageBezierHelper.setOnProgressChangedListener(new BookPageBezierHelper.OnProgressChangedListener() {
            @Override
            public void setProgress(int currentLength, int totalLength) {
                currentProgress = currentLength;
                //Log.e("TAG", currentLength + "    "  + currentProgress + "");
                float progress = currentLength * 100 / totalLength;
                textView.setText(String.format("%s%%", progress));
            }
        });

        currentBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        nextPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //设置bitmap，当前的图片和下一张的图片
        bookPageView.setBitmaps(currentBitmap, nextPageBitmap);
        bookPageView.setOnUserNeedSettingListener(new BookPageView.OnUserNeedSettingListener() {
            @Override
            public void onUserNeedSetting() {
                //// TODO: 2018/7/7 是菜单显示出来
                view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        if(getIntent() != null)
        {
            String bookPath = getIntent().getStringExtra("filePath");
            fileName = getIntent().getStringExtra("fileName");
            position = getIntent().getIntExtra("Position", 0);
            //判断file路径下的文件是不是为空
            if(!TextUtils.isEmpty(bookPath))
            {
                try {
                    bookPageBezierHelper.openBook(bookPath);
                    //draw两个canvas保证跳转书签的时候不会出错！
                    bookPageBezierHelper.draw(new Canvas(currentBitmap));
                    bookPageBezierHelper.draw(new Canvas(nextPageBitmap));
                    bookPageView.invalidate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("图书加载失败");
                dialog.show();
            }
        }
        else
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("图书加载失败");
            dialog.show();
        }
    }

    private List<String> getData(){
        List<String> data_list = new ArrayList<>();
        data_list.add("添加书签");
        data_list.add("读取书签");
        data_list.add("设置背景");
        data_list.add("语音朗读");
        data_list.add("跳转进度");
        return data_list;
    }
}
