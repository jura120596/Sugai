package com.github.jura120596.molodec.helpers;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CurrentToTopHelper extends CurrentViewAnimateHelper{

    public CurrentToTopHelper(int excludedPosition, float leftOffset) {
        this.excludedPosition = excludedPosition;
        this.leftOffset = leftOffset;
    }

    private int excludedPosition;
    private float leftOffset;

    @Override
    public void onUpdate(View view, int visiblePosition, int adapterPosition, double progress) {
        if (adapterPosition != excludedPosition) {
            view.setTranslationX((float) (-progress * leftOffset));
        }
    }

    public static void resetState(RecyclerView recyclerView, int childs) {
        for (int i = 0; i < childs; i++) {
            View v = recyclerView.getChildAt(i);
            if (v!=null) v.setTranslationX(0);
        }
    }
}
