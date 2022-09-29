package ru.sugai.village.helpers;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//https://habr.com/ru/company/homecredit/blog/489956/
abstract class AnimateHelper {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter attachedAdapter;
    private float currentFrameLeft = 0;

    public void attachToRecycler(RecyclerView recycler) {
        this.layoutManager = (LinearLayoutManager) recycler.getLayoutManager();
        this.recyclerView = recycler;
        this.attachedAdapter = (RecyclerView.Adapter) recycler.getAdapter();

        RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
            public void onChanged() {
                updatePositions();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                onChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                onChanged();
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                onChanged();
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                onChanged();
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                onChanged();
            }
        };
        attachedAdapter.registerAdapterDataObserver(dataObserver);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                updatePositions();
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    private void updatePositions() {
        int childCount = layoutManager.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = layoutManager.getChildAt(i);
            if (view == null) continue;
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            Float position = layoutManager.getOrientation() == RecyclerView.HORIZONTAL ?
                    (view.getLeft() - currentFrameLeft) / (view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin)
                    : (view.getTop() - currentFrameLeft) / (view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
            transformItemView(view, position);
        }
    }


    abstract void transformItemView(View view, Float position);
}
