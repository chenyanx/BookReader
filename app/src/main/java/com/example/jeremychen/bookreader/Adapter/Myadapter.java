package com.example.jeremychen.bookreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.jeremychen.bookreader.BookContentActivity;
import com.example.jeremychen.bookreader.Database.DatabaseHelper;
import com.example.jeremychen.bookreader.Database.DatabaseObject;
import com.example.jeremychen.bookreader.GsonObjects.Newbook;
import com.example.jeremychen.bookreader.R;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import cz.msebera.android.httpclient.Header;

/**
 * Created by jeremychen on 2018/7/5.
 */

public class Myadapter extends BaseAdapter {

    private List<Newbook.BookData> data_list;
    private Context m_context;
    private AsyncHttpClient client;
    private Dao<DatabaseObject, Integer> dao = null;

    public Myadapter(List<Newbook.BookData> data_list, Context m_context) {
        this.data_list = data_list;
        this.m_context = m_context;
    }

    @Override
    public int getCount() {
        return data_list.size();
    }

    @Override
    public Object getItem(int position) {
        return data_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        if(convertView == null)
        {
            convertView = LayoutInflater.from(m_context).inflate(R.layout.book_item_layout,null);
            holder.textView = (TextView) convertView.findViewById(R.id.text_view_content);
            holder.button = (Button) convertView.findViewById(R.id.button_download);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(data_list.get(position).getBookname());

        try {
            dao = DatabaseHelper.getInstance(m_context).getDao(DatabaseObject.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String path = Environment.getExternalStorageDirectory() + "/FreedomCYX/" + data_list.get(position).getBookname();
        final File file = new File(path);
        try {
            if(dao.queryForEq("fileName", data_list.get(position).getBookname()).size() == 0)
            {
                DatabaseObject object = new DatabaseObject();
                object.setId(position);
                object.setFileName(data_list.get(position).getBookname());
                object.setDownload_status(false);
                try {
                    dao.createOrUpdate(object);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            List<DatabaseObject> list = dao.queryForEq("fileName", data_list.get(position).getBookname());
            if(list.size()!=0)
            {
                if(list.get(0).isDownload_status() == false && file.exists())
                {
                    file.delete();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        holder.button.setText(file.exists()? "点击打开" : "点击下载");
        final ViewHolder finalHolder = holder;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file.exists())
                {
                    // TODO: 2018/7/6 设置点击打开的操作
                    Intent intent = new Intent(m_context, BookContentActivity.class);
                    intent.putExtra("filePath", path);
                    intent.putExtra("fileName", data_list.get(position).getBookname());
                    intent.putExtra("Position", position);
                    m_context.startActivity(intent);
                }
                else {
                    client = new AsyncHttpClient();
                    // TODO: 2018/7/6   此处代码很关键，是用来限定文件下载过程中是否需要压缩的
                    client.addHeader("Accept-Encoding", "identity");
                    client.get(data_list.get(position).getBookfile(), new FileAsyncHttpResponseHandler(file) {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                            finalHolder.button.setText("下载失败");
                            file.delete();
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            DatabaseObject object = new DatabaseObject();
                            object.setId(position);
                            object.setDownload_status(true);
                            object.setFileName(data_list.get(position).getBookname());
                            try {
                                dao.createOrUpdate(object);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Log.e("TAG", "文件已经下载完成，不需要下载，status已经改变成true");
                            finalHolder.button.setText("点击打开");
                            finalHolder.button.setClickable(true);
                        }
                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            finalHolder.button.setText(String.valueOf(bytesWritten*100/totalSize) + "%");
                        }
                    });
                }
            }
        });
        return convertView;
    }

    public class ViewHolder
    {
        TextView textView;
        Button button;
    }

}

