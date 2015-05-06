package com.example.dev8.myownparallax;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public abstract class BaseActivity extends AppCompatActivity {

    @Optional
    @InjectView(R.id.toolbar_actionbar)
    Toolbar toolbar;

    @Optional
    @InjectView(R.id.toolbarTitle)
    TextView toolbarTitle;

    protected ActionBar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.inject(this);

        if(hasToolbar()){
            setSupportActionBar(toolbar);
            supportActionBar = getSupportActionBar();
        }
    }

    public @ColorRes int getColor(int color){
        return getResources().getColor(color);
    }

    public abstract boolean hasToolbar();
    public abstract @LayoutRes int getLayout();
}
