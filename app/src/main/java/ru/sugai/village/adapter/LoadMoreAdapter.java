package ru.sugai.village.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.sugai.village.R;

import java.util.List;

public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public RecyclerView rv;
    public List<T> items;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private int visibleThreshold = 5;
    public Context context;
    private OnLoadMoreListener onLoadMoreListener;

    public LoadMoreAdapter(Context context, RecyclerView rv, List<T> items) {
        this.context = context;
        this.rv = rv;
        this.items = items;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv.getLayoutManager();
    }


    public Context getContext() {
        return context;
    }

    public List<T> getItems() {
        return items;
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : super.getItemViewType(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder != null && holder instanceof LoadMoreAdapter.LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }
}
