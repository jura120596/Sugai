package ru.sugai.village.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import ru.sugai.village.CONST.CONST;
import ru.sugai.village.R;
import ru.sugai.village.data.Photo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NewsPhotosViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Photo> imageUrls;


    public NewsPhotosViewPagerAdapter(Context context, ArrayList<Photo> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
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
        String url = CONST.SERVER_URl + imageUrls.get(position).getFile();
        RequestCreator requestCreator = Picasso.get().load(url).fit().centerCrop();
        requestCreator.into(imageView);
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(false);
                nagDialog.setContentView(R.layout.dialog_preview_image);
                FloatingActionButton btnClose = (FloatingActionButton) nagDialog.findViewById(R.id.preview_close);
                WebView web = nagDialog.findViewById(R.id.iv_preview_image);
//                web.setBackgroundColor(Color.BLACK);
                web.getSettings().setSupportZoom(true);
                web.getSettings().setBuiltInZoomControls(true);
                web.setPadding(0, 0, 0, 0);
                web.setScrollbarFadingEnabled(true);
                web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                web.getSettings().setLoadWithOverviewMode(true);
                web.getSettings().setUseWideViewPort(true);
                web.getSettings().setAllowFileAccess(true);
                web.getSettings().setAllowFileAccessFromFileURLs(true);
                String localPath = getBitmap(context, url);
                web.loadUrl("file://" + localPath);
                //                Picasso.get().load(CONST.SERVER_URl + imageUrls.get(position).getFile()).fit().centerCrop().into(ivPreview);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();
            }
        });
        return imageView;
    }
    public static String getBitmap(Context context, String url)
    {
        final String CACHE_PATH = context.getCacheDir().getAbsolutePath() + "/picasso-cache/";
        File[] files = new File(CACHE_PATH).listFiles();
        for (File file:files)
        {
            String fname= file.getName();
            if (fname.contains(".") && fname.substring(fname.lastIndexOf(".")).equals(".0"))
            {
                try
                {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String s = br.readLine();
                    if (s.equals(url))
                    {
                        String image_path =  CACHE_PATH + fname.replace(".0", ".1");
                        if (new File(image_path).exists())
                        {
                            return image_path;
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
