package com.example.jeremychen.bookreader.Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jeremychen on 2018/7/5.
 */

public class MyLoader extends AsyncTaskLoader<String> {

    private String download_url;

    public MyLoader(Context context, String download_url) {
        super(context);
        this.download_url = download_url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(isStarted())
        {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(download_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(6000);
            if(httpURLConnection.getResponseCode() == 200)
            {
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String content = null;
                while((content = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(content);
                }
                String data = stringBuilder.toString();
                return data;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private List<Book> dataResolver(String data) {
//        List<Book> list = new ArrayList<>();
//        try {
//            JSONObject jsonObject = new JSONObject(data);
//            JSONArray jsonArray = jsonObject.getJSONArray("data");
//            for(int i = 0; i < jsonArray.length(); i++)
//            {
//                Book book = new Book();
//                JSONObject temp_object = (JSONObject) jsonArray.get(i);
//                String filename = temp_object.getString("bookname");
//                String bookfile = temp_object.getString("bookfile");
//                book.setBookName(filename);
//                book.setBookFile(bookfile);
//                list.add(book);
//            }
//            return list;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
