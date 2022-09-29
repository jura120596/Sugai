package ru.sugai.village.ui.components;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import ru.sugai.village.R;
import ru.sugai.village.adapter.NewsPhotosViewPagerAdapter;
import ru.sugai.village.data.Photo;

import java.util.ArrayList;

public class ViewPagerCarouselView extends RelativeLayout {
    private FragmentManager fragmentManager;                // FragmentManager for managing the fragments withing the ViewPager
    private ViewPager vpCarousel;                           // ViewPager for the Carousel view
    private LinearLayout llPageIndicatorContainer;          // Carousel view item indicator, the little bullets at the bottom of the carousel
    private ArrayList<ImageView> carouselPageIndicators;    // Carousel view item, the little bullet at the bottom of the carousel
    private ArrayList<Photo> images;                        // Carousel view background image
    private long carouselSlideInterval;                     // Carousel view item sliding interval
    private Handler carouselHandler;                        // Carousel view item sliding interval automation handler

    public ViewPagerCarouselView (Context context) {
        super(context);
        initView(context);
    }

    public ViewPagerCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ViewPagerCarouselView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_pager_carousel_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        vpCarousel = (ViewPager) this.findViewById(R.id.vp_carousel);
        llPageIndicatorContainer = (LinearLayout) this.findViewById(R.id.ll_page_indicator_container);
    }

    /**
     * Set the data and initialize the carousel view
     * @param fragmentManager
     * @param imageResourceIds
     * @param carouselSlideInterval
     */
    public void setData(FragmentManager fragmentManager, ArrayList<Photo> imageResourceIds, long carouselSlideInterval) {
        onDestroy();
        this.fragmentManager = fragmentManager;
        this.images = imageResourceIds;
        this.carouselSlideInterval = carouselSlideInterval;
        initData();
        initCarousel();
        initCarouselSlide();
    }

    /**
     * Initialize the data for the carousel
     */
    private void initData() {
        carouselPageIndicators = new ArrayList<>();
        llPageIndicatorContainer.removeAllViews();
        for (int i = 0; i < images.size(); i++) {
            ImageView obj = new ImageView(getContext());
            obj.setImageResource(R.drawable.selector_carousel_page_indicator);
            obj.setPadding(0, 0, 5, 0); // left, top, right, bottom
            llPageIndicatorContainer.addView(obj);
            carouselPageIndicators.add(obj);
        }
    }

    /**
     * Initialize carousel views, each item in the carousel view is a fragment
     */
    private void initCarousel() {
        carouselPageIndicators.get(0).setSelected(true);

        // Update the carousel page indicator on change
        vpCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < carouselPageIndicators.size(); i++) {
                    if (i == position)
                        carouselPageIndicators.get(position).setSelected(true);
                    else
                        carouselPageIndicators.get(i).setSelected(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        ViewPagerCarouselAdapter viewPagerCarouselAdapter = new ViewPagerCarouselAdapter(fragmentManager, images);
//        vpCarousel.setPageTransformer(false, new CustomViewPageTransformer(CustomViewPageTransformer.TransformType.SLIDE_OVER));
//        vpCarousel.setAdapter(viewPagerCarouselAdapter);
        NewsPhotosViewPagerAdapter a = new NewsPhotosViewPagerAdapter(getContext(), images);
        vpCarousel.setAdapter(a);
    }

    /**
     * Handler to make the view pager to slide automatically
     */
    private void initCarouselSlide() {
        final int nCount = images.size();
        try {
            carouselHandler = new Handler();
            carouselHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int curPos = vpCarousel.getCurrentItem();
                    curPos++;
                    if (curPos == nCount) curPos = 0;
                    vpCarousel.setCurrentItem(curPos, true);
                    carouselHandler.postDelayed(this, carouselSlideInterval);
                }
            }, carouselSlideInterval);

        } catch (Exception e) {
            Log.d("MainActivity", e.getMessage());
        }
    }

    public void onDestroy() {
        if (carouselHandler != null) carouselHandler.removeCallbacksAndMessages(null); // remove call backs to prevent memory leaks
    }
}
