package com.example.dev8.myownparallax;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import butterknife.InjectView;

public class ParallaxImageScrollViewActivity extends BaseActivity implements ObservableScrollViewCallbacks{

    @InjectView(R.id.scrollview)
    ObservableScrollView scrollview;

    @InjectView(R.id.image_container)
    FrameLayout imageContainer;

    @InjectView(R.id.image)
    ImageView image;

    private Integer paletteColor = null;
    private ToolbarState toolbarState = ToolbarState.TOOLBAR_STATE_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showToolbarTitle(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbarTitle.setText("Cavaco Silva");
        toolbarTitle.setVisibility(View.INVISIBLE);

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.wallpapers66);

        Palette palette = Palette.generate(imageBitmap);
        Palette.Swatch swatch = palette.getDarkVibrantSwatch();

        if (swatch != null) {
            paletteColor = swatch.getRgb();
            imageContainer.setBackgroundColor(paletteColor);
        }
        setToolbarColor(ToolbarState.TOOLBAR_STATE_TRANSPARENT);
        scrollview.setScrollViewCallbacks(this);
    }

    public void setToolbarColor(ToolbarState state){
        if(!toolbarState.equals(state)) {
            toolbar.setBackgroundColor(state.isTransparent() ? getColor(android.R.color.transparent) : paletteColor);
            toolbarState = state;
        }
    }

    @Override
    public boolean hasToolbar() {
        return true;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    public void showToolbarTitle(boolean showTitle){
        if(supportActionBar != null)
            supportActionBar.setDisplayShowTitleEnabled(showTitle);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        imageContainer.setTranslationY(Math.max(0, -(getFlexibleSpace() - scrollY)));
        setImageAlpha(scrollY);

        if(scrollY <= getFlexibleSpace()*2) {
            imageContainer.setTranslationY(scrollY * 0.5f);
        }

        if(scrollY > getFlexibleSpace()) {
            setToolbarColor(ToolbarState.TOOLBAR_STATE_NORMAL);
            showViewFadeAnimation(true, toolbarTitle, 300);
        }else{
            setToolbarColor(ToolbarState.TOOLBAR_STATE_TRANSPARENT);
            showViewFadeAnimation(false, toolbarTitle, 300);
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
        return imageContainer.getHeight() - toolbar.getHeight();
    }

    public void setImageAlpha(int scrollY) {
        float alpha = 0 + (float) Math.max(0, getFlexibleSpace() - scrollY) / getFlexibleSpace();
        image.setAlpha(alpha);
    }
    enum ToolbarState {
        TOOLBAR_STATE_NORMAL,
        TOOLBAR_STATE_TRANSPARENT;

        public boolean isNormal(){
            return this.equals(ToolbarState.TOOLBAR_STATE_NORMAL);
        }

        public boolean isTransparent(){
            return this.equals(ToolbarState.TOOLBAR_STATE_TRANSPARENT);
        }
    }
}
