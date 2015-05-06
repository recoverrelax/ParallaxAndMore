package com.example.dev8.myownparallax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean hasToolbar() {
        return false;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.parallax_fade_scrollview)
    public void OnParallaxFadeSrollviewClick(View view){
        startActivity(new Intent(this, ParallaxImageScrollViewActivity.class));
    }

    @OnClick(R.id.parallax_play)
    public void OnParallaxPlayClick(View view){
        startActivity(new Intent(this, ParallaxLikeGooglePlayActivity.class));
    }

    @OnClick(R.id.ioshed)
    public void OnParallaxIoshedClick(View view){
        startActivity(new Intent(this, ParallaxLikeIoshedActivity.class));
    }
}
