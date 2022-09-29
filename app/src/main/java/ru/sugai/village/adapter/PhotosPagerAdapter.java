package ru.sugai.village.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import ru.sugai.village.CONST.CONST;
import ru.sugai.village.data.Photo;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class PhotosPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Photo> imageUrls;


    public PhotosPagerAdapter(Context context, ArrayList<Photo> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }
        @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==object;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Photo photo = imageUrls.get(position);
        if (photo.getId() > 0) {
            Picasso.get().load(CONST.SERVER_URl + photo.getFile()).fit().centerCrop().into(imageView);
        } else {
            Picasso.get().load(new File(photo.getFile())).fit().centerCrop().into(imageView);
        }
        container.addView(imageView);
        return imageView;


    }
}
