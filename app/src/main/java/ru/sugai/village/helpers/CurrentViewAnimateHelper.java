package ru.sugai.village.helpers;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//https://habr.com/ru/company/homecredit/blog/489956/
abstract class CurrentViewAnimateHelper {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    public void attachToRecycler(RecyclerView recycler) {
        this.layoutManager = (LinearLayoutManager) recycler.getLayoutManager();
        this.recyclerView = recycler;
    }

    public void update(float progress) {
        int childCount = layoutManager.getChildCount();
        for (int i = 0; i <childCount; i++) {
            View view = layoutManager.getChildAt(i);
            if (view == null) continue;
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            float visiblePosition =  (layoutManager.getOrientation() == RecyclerView.HORIZONTAL)
                ? 1F*view.getRight() / (view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin)
                : 1F*view.getBottom() / (view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            int adapterPosition = recyclerView.getChildAdapterPosition(view);
            onUpdate(view, (int) Math.ceil(visiblePosition), adapterPosition, progress);
        }
    }
    public abstract void onUpdate(View view, int visiblePosition, int adapterPosition, double progress);
}
