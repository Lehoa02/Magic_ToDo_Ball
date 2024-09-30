package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private IntroPagerAdapter introPagerAdapter;
    private Button btnNext, btnCnt;
    private TextView tvSkip;
    private TextView title, discrp;
    private ImageView imageView;
    Animation btnAnim;



    private int currentPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class );
            startActivity(mainActivity);
            finish();


        }
        setContentView(R.layout.activity_intro);
         btnNext = findViewById(R.id.btn_next);
         btnCnt = findViewById(R.id.btn_get_started);
         tvSkip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.screen_viewpager);
        viewPager.setAdapter(introPagerAdapter);
        imageView = findViewById(R.id.intro_img);
        title = findViewById(R.id.intro_title);
        discrp = findViewById(R.id.intro_description);
        introPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());

        List<IntroScreenItem> screenItems  = new ArrayList<>();
        screenItems.add(new IntroScreenItem(R.drawable.img1, "Welcome to Magic ToDo Ball", "This app will help you keep up with your plans in a fun way. Just shake the ball and the Universe will show what to do..."));
        screenItems.add(new IntroScreenItem(R.drawable.img2, "Actual ToDo List", "But if you got tired of \"unserious\" design you always can switch to a casual-looking ToDo List"));

        final IntroScreenItem[] currentItem = new IntroScreenItem[1];
        currentItem[0] = screenItems.get(currentPage);
        if (!screenItems.isEmpty()) {
           // currentItem = screenItems.get(currentPage);
            imageView.setImageResource(currentItem[0].getImageResource());
            title.setText(currentItem[0].getTitle());
            discrp.setText(currentItem[0].getDescription());
        }


        // Add fragments/screens here (or use layout ids directly)




        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        //tvSkip.setVisibility(View.GONE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentPage < screenItems.size() - 1) {

                    currentPage++;
                    viewPager.setCurrentItem(currentPage);
                    loaddLastScreen();
                    currentItem[0] = screenItems.get(currentPage);

                    // Update the UI with the data from the next item
                    imageView.setImageResource(currentItem[0].getImageResource());
                    title.setText(currentItem[0].getTitle());
                    discrp.setText(currentItem[0].getDescription());
                } else {
                        launchMainActivity();
                    Log.d("IntroActivity", "End of list reached");
                    }}
        });

        btnCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        tvSkip.setOnClickListener(view -> launchMainActivity());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (position == screenItems.size() - 1) {
                    btnNext.setText("Start");
                    tvSkip.setVisibility(View.GONE);
                } else {
                    btnNext.setText("Next");
                    tvSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }




    //introPagerAdapter.addFragment(new ScreenFragment1());
       // introPagerAdapter.addFragment(new ScreenFragment2());



        // Other setup configurations for ViewPager

    private void moveToNextFragment() {
        ViewPager viewPager = findViewById(R.id.screen_viewpager);
        int currentItem = viewPager.getCurrentItem();
        int nextItem = currentItem + 1;
        viewPager.setCurrentItem(nextItem);
    }
    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }

    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnCnt.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnCnt.setAnimation(btnAnim);



    }
    private void launchMainActivity() {
        // Save intro completion state in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("intro", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("introCompleted", true);
        editor.apply();

        // Launch the main activity
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the IntroActivity
    }
}

