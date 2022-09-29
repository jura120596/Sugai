package ru.sugai.village.ui.components;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.InputStream;

public class GifView extends View {

    private Movie mMovie;
    private InputStream mStream;
    private long mMoviestart;
    private float mMovieWidth;
    private float mMovieHeight;
    private long mMovieDuration;
    private float scale = 1;

    public GifView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public GifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GifView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GifView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public GifView(Context context, InputStream stream) {
        super(context);

        mStream = stream;
        mMovie = Movie.decodeStream(mStream);

        mMovieWidth = mMovie.width();
        mMovieHeight = mMovie.height();
        mMovieDuration = mMovie.duration();
    }

    public GifView(Context context, int resId) {
        super(context);
        setResource(resId);
    }
    public void setResource(int id) {
        InputStream stream = getContext().getResources().openRawResource(id);
        mMovie = Movie.decodeStream(stream);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        scale = dm.widthPixels * 1F / mMovie.width();
        mMovieWidth = mMovie.width()*scale;
        mMovieHeight = mMovie.height()*scale;
        mMovieDuration = mMovie.duration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int)mMovieWidth, (int)mMovieHeight);
    }

    public float getMovieWidth() {
        return mMovieWidth;
    }

    public float getMovieHeight() {
        return mMovieHeight;
    }

    public long getMovieDuration() {
        return mMovieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        final long now = SystemClock.uptimeMillis();
        if (mMoviestart == 0) {
            mMoviestart = now;
        }
        final int relTime = (int) ((now - mMoviestart) % mMovie.duration());
        mMovie.setTime(relTime);
        canvas.save();
        canvas.scale(scale, scale);
        mMovie.draw(canvas, 0, 0);
        canvas.restore();
        this.invalidate();
    }
}