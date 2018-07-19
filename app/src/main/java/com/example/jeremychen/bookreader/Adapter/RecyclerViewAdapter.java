package com.example.jeremychen.bookreader.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jeremychen on 2018/7/7.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder> {

    private Context context;
    private List<String> data_list;
    private OnClickListener m_onClickListener;

    public RecyclerViewAdapter(Context context, List<String> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(context);
        return new MainViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        final TextView textView = holder.textView;
        textView.setHeight(180);
        textView.setWidth(240);
        textView.setTextSize(16);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setText(data_list.get(position));


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_onClickListener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MainViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }


    public interface OnClickListener{
        void OnClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener)
    {
        m_onClickListener = onClickListener;
    }
}
