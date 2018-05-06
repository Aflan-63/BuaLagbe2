package com.lagbe.bua.aflan.bualagbe;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;
    Button login,switchAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);
        if(!(SaveSharedPreference.getUserName(MainActivity.this).length() == 0))
        {
            Intent myIntent = new Intent(MainActivity.this, ServicesActivity.class);
            overridePendingTransition(R.transition.activity_fade, R.transition.activity_slide);
            //myIntent.putExtra("key", value); //Optional parameters
            //startActivityForResult(myIntent, 1);
            startActivity(myIntent);
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            //  ActionBar actionBar = getActionBar();
//            actionBar.hide();
            setContentView(R.layout.activity_main);
        }
        login = (Button)findViewById(R.id.login_acc);
        //witchAccount = (Button)findViewById(R.id.switch_acc);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.relativeLayout);


                if (fragment == null) {
                    fragment = new GPlusFragment();
                    fm.beginTransaction()
                            .add(R.id.relativeLayout, fragment)
                            .commit();

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }
}
