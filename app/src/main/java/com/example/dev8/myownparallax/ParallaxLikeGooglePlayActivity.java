package com.example.dev8.myownparallax;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import butterknife.InjectView;
import butterknife.OnTouch;

public class ParallaxLikeGooglePlayActivity extends BaseActivity implements ObservableScrollViewCallbacks{

    @InjectView(R.id.scrollview)
    ObservableScrollView scrollview;

    @InjectView(R.id.image_container)
    FrameLayout imageContainer;

    private int toolbarColor;
    private ToolbarState toolbarState = ToolbarState.TOOLBAR_STATE_NORMAL;
    private int oldScrollY = 0;
    private int lastScrollYDirection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showToolbarTitle(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbarTitle.setText("Cavaco Silva");
        toolbarTitle.setVisibility(View.INVISIBLE);

        toolbarColor = getColor(R.color.primaryColor);

        setToolbarColor(ToolbarState.TOOLBAR_STATE_TRANSPARENT);
        scrollview.setScrollViewCallbacks(this);
    }

    public void setToolbarColor(ToolbarState state){
        if(!toolbarState.equals(state)) {
            toolbar.setBackgroundColor(state.isTransparent() ? getColor(android.R.color.transparent) : toolbarColor);
            toolbarState = state;
        }

        if(state.equals(ToolbarState.TOOLBAR_STATE_NORMAL))
            toolbarTitle.setVisibility(View.VISIBLE);
        else if(state.equals(ToolbarState.TOOLBAR_STATE_TRANSPARENT))
            toolbarTitle.setVisibility(View.INVISIBLE);
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

    /**
     * This listener will allow us to hide back the toolbar once the solid toolbar is shown, the last scrolling was scroll-down and the scrolling
     * has ended.
     * Basically, if the user scrolls enough down so that when he scroll up again the solid toolbar is shown, the same can be partially hidden
     * if the user suddenly stops scrolling. For this reason, if the solid toolbar is shown, the last scroll action was down and the solid-toolbar is
     * not on its right position, the toolbar will hide itself automatically.
     */
    @OnTouch(R.id.scrollview)
    public boolean onScrollViewTouch(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(toolbar.getTranslationY() != 0 && toolbarState.equals(ToolbarState.TOOLBAR_STATE_NORMAL) && lastScrollYDirection == 1){ // UP
                final AlphaAnimation fadeIn = new AlphaAnimation(1.0f, 0.0f);
                fadeIn.setDuration(300);
                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toolbar.setVisibility(View.INVISIBLE);
                        setToolbarColor(ToolbarState.TOOLBAR_STATE_TRANSPARENT);
                        toolbar.setTranslationY(-toolbar.getHeight());
                        toolbar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                toolbar.startAnimation(fadeIn);
            }
        }
        return false;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        /**
         * Set the Parallax effect through translation. We only need this.
         * While the whole view scrolls 1x, the image scrolls 0.5x
         */
        imageContainer.setTranslationY(scrollY * 0.5f);

        /**
         * This Condition will move the Toolbar Up, after the imageContainer bottom border reaches the toolbar bottom border
         * This effect will occur as long as the Toolbar is in transparent mode. when the toolbar is in color mode, the interactions
         * will be different.
         */
        if(scrollY-getFlexibleSpace() < toolbar.getHeight() && toolbarState.equals(ToolbarState.TOOLBAR_STATE_TRANSPARENT))
            toolbar.setTranslationY(Math.min(0, -scrollY + getFlexibleSpace()));

        /**
         * After a certain amount of scroll down after the image is hidden, the toolbar can be shows again by Scrolling-UP.
         * For that, first we need to reposition the toolbar back in its right position, which is up immediately after the top offscreen. This is because
         * the toolbar will continuously scrolling, even after offscreen.
         *
         * We also change its state (the backgroundColor) back to normal, although, at first, it is invisible (offscreen).         *
         * So after this point, the toolbar is immediately after the topBorder of the screen and it visible.
         */
        if(scrollY >= imageContainer.getHeight()*2 && isScrollDown(scrollY) && !toolbarState.equals(ToolbarState.TOOLBAR_STATE_NORMAL)){
            setToolbarColor(ToolbarState.TOOLBAR_STATE_NORMAL);
            toolbar.setVisibility(View.INVISIBLE);
            toolbar.setTranslationY(-toolbar.getHeight());
            toolbar.setVisibility(View.VISIBLE);
        }
        /**
         * So, after the toolbar is hidden, and enough scroll is 'scrolled' down, if we scroll-Up, the toolbar will be shown. The following
         * method translates the toolbar (slide top/offscreen - bottom) till its visible.
         * One thing here, if the amount of scroll is too big, the scroll of the toolbar will be iinstantaneous while if it is slow, the toolbar
         * scroll speed will match the scroll speed.
         */

        if (isScrollDown(scrollY) && toolbarState.equals(ToolbarState.TOOLBAR_STATE_NORMAL) && toolbar.getTranslationY() < 0 && (scrollY-oldScrollY != 0)) {
            if(toolbar.getTranslationY() + Math.abs(scrollY-oldScrollY) <= 0)
                toolbar.setTranslationY(toolbar.getTranslationY() + Math.abs(scrollY-oldScrollY));
            else
                toolbar.setTranslationY(0);
        }

        /**
         * This method is the same as the previous one, but the reverse scrolling, Scrolling-UP.
         */

        if (isScrollUp(scrollY) && toolbarState.equals(ToolbarState.TOOLBAR_STATE_NORMAL) && toolbar.getTranslationY() <= 0 && (scrollY-oldScrollY != 0)) {
            if(toolbar.getTranslationY() - Math.abs(scrollY-oldScrollY) > -toolbar.getHeight())
                toolbar.setTranslationY(toolbar.getTranslationY() - Math.abs(scrollY-oldScrollY));
            else
                toolbar.setTranslationY(-toolbar.getHeight());
        }

        /**
         * If the user scrolled enough down so a Scroll-UP will show the solid toolbar, the same wont become transparent again unless the imageContainer
         * visible part in the screen has the same Height as the toolbar height. When that amount of scroll is reached, we fade the toolbar back
         * to its original state, which is transparent. The title is only shown in solid mode, so when changing modes we need to hide the text as well.
         */

        if(imageContainer.getTranslationY()*2 <= getFlexibleSpace() && toolbarState.equals(ToolbarState.TOOLBAR_STATE_NORMAL)) {
            final ObjectAnimator colorFade = ObjectAnimator.ofObject(toolbar, "backgroundColor", new ArgbEvaluator(), toolbarColor, android.R.color.transparent);
            colorFade.setDuration(300);
            colorFade.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbarTitle.setVisibility(View.INVISIBLE);
                }
            }, 200);
            toolbarState = ToolbarState.TOOLBAR_STATE_TRANSPARENT;
        }

        setScrollDirections(scrollY);
    }

    /**
     * This refers to the following 3 methods
     *
     * The onUpOrCancelMotionEvent() listener provided by the Observable library allow us to listen for Scroll-Up and Scroll-Down events
     * but won't fit this case because it will only notify us of Scroll-Directions after the scrolling has ended. We need a way to get
     * the scroll-direction while the user is still scrolling, not when he ended it. lastScrollDirection will contain that information,
     * 1 meaning Scroll-UP and -1 meaning Scroll-Down.
     *
     */
    private void setScrollDirections(int scrollY) {
        if(scrollY > oldScrollY)
            lastScrollYDirection = 1;
        if(scrollY < oldScrollY)
            lastScrollYDirection = -1;

        oldScrollY = scrollY;
    }

    private boolean isScrollDown(int scrollY) {
        return scrollY <= oldScrollY && lastScrollYDirection == -1;
    }

    private boolean isScrollUp(int scrollY){
        return scrollY >= oldScrollY && lastScrollYDirection == 1;
    }

    @Override
    public void onDownMotionEvent() {}

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {}

    /**
     * Flexible Space is the name given to the space between the imageContainer bottom border, and the toolbar bottom border.
     */
    public int getFlexibleSpace(){
        return imageContainer.getHeight() - toolbar.getHeight();
    }

    /**
     * Enum used to keep track of tollbar states. When its on transparent mode, and when its on solid-color mode.
     */
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
