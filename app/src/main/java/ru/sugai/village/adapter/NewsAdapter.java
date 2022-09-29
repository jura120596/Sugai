package ru.sugai.village.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ru.sugai.village.R;
import ru.sugai.village.data.News;
import ru.sugai.village.data.Photo;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.helpers.LastItemListener;
import ru.sugai.village.ui.components.ViewPagerCarouselView;

import java.util.ArrayList;
import java.util.Locale;


public class NewsAdapter extends RecyclerView.Adapter {
    public Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    private ClickListener clickListener;
    private LastItemListener lastItemListener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).bindView(position);
    }


    @Override
    public int getItemCount() {
        return DataBASE.NEWS_JSON_LIST.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView content, zagal, dateTV;
        ViewPagerCarouselView viewPager;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewPager);
            content = itemView.findViewById(R.id.news_content);
            dateTV = itemView.findViewById(R.id.news_content_date);
            zagal = itemView.findViewById(R.id.news_title);
            viewPager.setVisibility(View.GONE);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            if (position == DataBASE.NEWS_JSON_LIST.size() - 1 && lastItemListener != null) lastItemListener.onLastItemOpened(position);
            News news = DataBASE.NEWS_JSON_LIST.get(position);
            String title = "" + news.getTitle().replaceAll("<P>", "");
            title = title.trim().substring(0,1).toUpperCase() + title.trim().substring(1).toLowerCase(Locale.ROOT);
            zagal.setText(title);
            dateTV.setText(news.getDate());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                content.setText(Html.fromHtml(news.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                content.setText(Html.fromHtml(news.getDescription()));
            }
            ArrayList<Photo> photos = DataBASE.NEWS_JSON_LIST.get(position).getPhotos();
            if (photos.size()> 0) {
                viewPager.setData(((AppCompatActivity)context).getSupportFragmentManager(), photos, 5000);
                viewPager.setVisibility(View.VISIBLE);
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
        this.clickListener = clickListener;
    }

    public void setLastItemListener(LastItemListener lastItemListener) {
        this.lastItemListener = lastItemListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View view);

        void onItemLongClick(int position, View view);
    }
}
