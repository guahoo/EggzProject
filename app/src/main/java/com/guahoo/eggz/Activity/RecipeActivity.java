package com.guahoo.eggz.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.guahoo.eggz.Activity.StartActivity;
import com.guahoo.eggz.R;
import com.guahoo.eggz.Utility.ResourcesModel;
import com.guahoo.eggz.Utility.SimplePagerAdapter;

import java.util.Objects;


public class RecipeActivity extends AppCompatActivity {

    SimplePagerAdapter simplePagerAdapter;
    Button finishButton;
    final String TAG = "";
    Button crossButton;
    SharedPreferences sharedPreferences;
    String PREFERENCES;

    public int getCurrentPage() {
        return currentPage;
    }

    private int currentPage;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LOCKED );
        requestWindowFeature ( Window.FEATURE_NO_TITLE );
       // getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        if (getSupportActionBar () != null) {
            getSupportActionBar ().setDisplayShowTitleEnabled ( false );
            getSupportActionBar ().hide ();
        }
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_recipe );
        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);


        simplePagerAdapter = new SimplePagerAdapter ( this );
        crossButton = findViewById ( R.id.backButton );
        finishButton = findViewById ( R.id.startButton );

        if (Objects.equals(sharedPreferences.getString("languague", null), "EN")) {
            finishButton.setBackgroundResource(R.drawable.ic_button_start_recipe_en);
        }
        finishButton.setVisibility ( View.INVISIBLE );



/**exit to menu**/
        crossButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( getApplicationContext (), StartActivity.class );
                intent.putExtra ( "startMenu","startMenu" );
                startActivity ( intent );

            }
        } );
/**exit ti mainActivity**/

        finishButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( getApplicationContext (), StartActivity.class );
                startActivity ( intent );
            }
        } );


        ViewPager pager =  findViewById ( R.id.vpager );
        if (pager != null) {
            pager.setAdapter ( new SimplePagerAdapter ( this ) );
        }


        PageListener pageListener = new PageListener ();
        pager.setOnPageChangeListener ( pageListener );
        pager.getCurrentItem ();
    }

    /**viewPager**/

    public  class PageListener extends ViewPager.SimpleOnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled ( position, positionOffset, positionOffsetPixels );

        }

        public int getCurrentPage() {
            return currentPage;
        }




        public void onPageSelected(int position) {
            Log.i(TAG, "page selected " + position);
            currentPage = position;
            if (position==5){
                finishButton.setVisibility ( View.VISIBLE );
                crossButton.setVisibility ( View.INVISIBLE );
            }else if(position<5){
                finishButton.setVisibility ( View.INVISIBLE );
                crossButton.setVisibility ( View.VISIBLE );
            }
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }




    }




