package com.example.dev8.myownparallax;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import butterknife.InjectView;

public class ParallaxLikeIoshedActivity extends BaseActivity implements ObservableScrollViewCallbacks{

    @InjectView(R.id.scrollview)
    ObservableScrollView scrollview;

    @InjectView(R.id.image_container)
    FrameLayout imageContainer;

    @InjectView(R.id.parent)
    RelativeLayout parent;

    @InjectView(R.id.header)
    LinearLayout header;

    @InjectView(R.id.Ok)
    FloatingActionButton ok_button;

    private Integer paletteColor = null;
    private final int FLOATBUTTON_FIXED_TRANSLATIONY = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showToolbarTitle(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbarTitle.setText("PARALLAX + FADING IMAGEVIEW");
        toolbarTitle.setVisibility(View.INVISIBLE);
        ok_button.setTranslationY(FLOATBUTTON_FIXED_TRANSLATIONY);
        ok_button.setVisibility(View.INVISIBLE);

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.wallpapers66);

        Palette palette = Palette.generate(imageBitmap);
        Palette.Swatch swatch = palette.getVibrantSwatch();

        if (swatch != null) {
            paletteColor = swatch.getRgb();
            imageContainer.setBackgroundColor(paletteColor);
        }
        toolbar.setBackgroundColor(paletteColor);
        header.setBackgroundColor(paletteColor);
        parent.setBackgroundColor(paletteColor);

        scrollview.setScrollViewCallbacks(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ScaleAnimation scaleAnim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnim.setDuration(1000);
                scaleAnim.setFillAfter(true);
                ok_button.startAnimation(scaleAnim);
                ok_button.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    @Override
    public boolean hasToolbar() {
        return true;
    }

    @Override
    public int getLayout() {
        return R.layout.parallax_like_ioshed;
    }

    public void showToolbarTitle(boolean showTitle){
        if(supportActionBar != null)
            supportActionBar.setDisplayShowTitleEnabled(showTitle);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        header.setTranslationY(Math.max(0, -(getFlexibleSpace() - scrollY)));
        ok_button.setTranslationY(Math.max(FLOATBUTTON_FIXED_TRANSLATIONY, -(getFlexibleSpace() - scrollY)+FLOATBUTTON_FIXED_TRANSLATIONY));

        if(scrollY <= getFlexibleSpace()*2) {
            imageContainer.setTranslationY(scrollY * 0.5f);
        }
    }

    @Override
    public void onDownMotionEvent() {}

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {}

    public int getFlexibleSpace(){
        return imageContainer.getHeight();
    }
}
