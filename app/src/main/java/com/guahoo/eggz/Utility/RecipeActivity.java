package com.guahoo.eggz.Utility;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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


public class RecipeActivity extends AppCompatActivity {

    SimplePagerAdapter simplePagerAdapter;
    ResourcesModel resourcesModel;
    Button finishButton;
    private  final String TAG = "";
    Button crossButton;

    public int getCurrentPage() {
        return currentPage;
    }

    private int currentPage;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LOCKED );
        requestWindowFeature ( Window.FEATURE_NO_TITLE );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        if (getSupportActionBar () != null) {
            getSupportActionBar ().setDisplayShowTitleEnabled ( false );
            getSupportActionBar ().hide ();
        }
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_recipe );


        simplePagerAdapter = new SimplePagerAdapter ( this );
        crossButton = findViewById ( R.id.backButton );
        finishButton = findViewById ( R.id.startButton );
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


        ViewPager pager = (ViewPager) findViewById ( R.id.vpager );
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




    }




