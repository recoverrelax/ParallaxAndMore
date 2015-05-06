package com.example.dev8.myownparallax;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

    private Integer paletteColor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showToolbarTitle(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbarTitle.setText("PARALLAX + FADING IMAGEVIEW");
        toolbarTitle.setVisibility(View.INVISIBLE);

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

        if(scrollY <= getFlexibleSpace()*2) {
            imageContainer.setTranslationY(scrollY * 0.5f);
        }
    }

    public void showViewFadeAnimation(boolean visibility, final View view, int animationDuration) {
        if (visibility && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(animationDuration);
            view.startAnimation(fadeIn);
        }
        if (!visibility && view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
            AlphaAnimation fade_out = new AlphaAnimation(1.0f, 0.0f);
            fade_out.setDuration(animationDuration);
            view.startAnimation(fade_out);
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
