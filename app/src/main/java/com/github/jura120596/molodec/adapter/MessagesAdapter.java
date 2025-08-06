package com.github.jura120596.molodec.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.data.Message;
import com.github.jura120596.molodec.data.database.DataBASE;

import java.util.ArrayList;


public class MessagesAdapter extends RecyclerView.Adapter {
    public Context context;
    private ArrayList<Message> list;


    public MessagesAdapter(Context context, ArrayList<Message> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).fromOtherUser() ? 1 : 0;
    }

    public Context getContext() {
        return context;
    }

    private static ClickListener clickListener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
        else view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).bindView(position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView from, text, dateTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.lblMsgFrom);
//            dateTV = itemView.findViewById(R.id.news_content_date);
            text = itemView.findViewById(R.id.txtMsg);
//            itemView.setOnLongClickListener(this);
//            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            Message message = list.get(position);
            from.setText(message.getUser().getFull_name());
            if (message.getUser().getId() == DataBASE.user.getId()) from.setVisibility(View.GONE);
//            dateTV.setText(news.getDate());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text.setText(Html.fromHtml(message.getText(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                text.setText(Html.fromHtml(message.getText()));
            }
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAdapterPosition(), view);
            return false;
        }

    }

    public void setClickListener(ClickListener clickListener) {
        MessagesAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(int position, View view);

        void onItemLongClick(int position, View view);
    }
}
