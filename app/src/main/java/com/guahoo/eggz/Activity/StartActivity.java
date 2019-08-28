package com.guahoo.eggz.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.guahoo.eggz.Utility.Dialog_menu;
import com.guahoo.eggz.Utility.InitString;
import com.guahoo.eggz.Utility.PlaySound;
import com.guahoo.eggz.R;
import com.guahoo.eggz.Utility.SoundPoolHelper;

import java.util.Objects;

public class StartActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    Button softEggzButton;
    Button mediumEggzButton;
    Button hardEgggzButton;
    Button menuButton;
    ImageView titleView;
    Button button_custom_picker_show;
    boolean languague_menu_is_active;
    Button buttonsoundOn;
    Button buttonsoundOff;
    Button languague_Button;
    Button russian_Languegue_Button;
    Button english_Languegue_Button;
    ImageView yellow_Langugue_Background;
    SharedPreferences sharedPreferences;
    SoundPoolHelper mSoundPoolHelper;
    int mSoundLessId;
    InitString initString;
    String PREFERENCES;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LOCKED );
        requestWindowFeature ( Window.FEATURE_NO_TITLE );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        if (getSupportActionBar () != null) {
            getSupportActionBar ().setDisplayShowTitleEnabled ( false );
            getSupportActionBar ().hide ();
        }
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_start );

        softEggzButton = findViewById ( R.id.softEggzButton );
        mediumEggzButton = findViewById ( R.id.mediumEggzbutton );
        hardEgggzButton = findViewById ( R.id.hardEggzButton );
        titleView = findViewById ( R.id.titleView );
        menuButton = findViewById ( R.id.menuButton );
        button_custom_picker_show = findViewById ( R.id.button_custom_picker_show );


        buttonsoundOff = findViewById ( R.id.bell_off_button );
        buttonsoundOn = findViewById ( R.id.bell_on_button );
        yellow_Langugue_Background = findViewById ( R.id.languague_yellow_background );
        languague_Button = findViewById ( R.id.languague_button );
        russian_Languegue_Button = findViewById ( R.id.russian_button );
        english_Languegue_Button = findViewById ( R.id.english_button );
        languague_menu_is_active = false;
        sharedPreferences = getApplicationContext ().getSharedPreferences ( PREFERENCES, MODE_PRIVATE );
        mSoundPoolHelper = new SoundPoolHelper(1, this);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.click, 1);
        initString = new InitString();
        setEnLanguage ();



        menuButton.setOnClickListener ( new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Dialog_menu dialog_menu = new Dialog_menu ( buttonsoundOn, buttonsoundOff,
                        languague_Button, russian_Languegue_Button, english_Languegue_Button,
                        yellow_Langugue_Background, sharedPreferences, languague_menu_is_active, StartActivity.this );
                dialog_menu.showMenuDialog ();
            }
        } );

        button_custom_picker_show.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                show ();
            }
        } );

        softEggzButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startMainActivity ( "soft" );
            }
        } );

        mediumEggzButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startMainActivity ( "medium" );
            }
        } );

        hardEgggzButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startMainActivity ( "hard" );
            }
        } );
    }


    /**
     * диалог кастом таймера
     **/
    protected void show() {
        final Dialog d = new Dialog ( StartActivity.this );
        d.requestWindowFeature ( Window.FEATURE_NO_TITLE );
        d.getWindow ().setBackgroundDrawable ( new ColorDrawable ( android.graphics.Color.TRANSPARENT ) );
        d.setContentView ( R.layout.dialog );
        Button b1 = (Button) d.findViewById ( R.id.button1 );


        final NumberPicker np = (NumberPicker) d.findViewById ( R.id.numberPicker );
        final NumberPicker np2 = (NumberPicker) d.findViewById ( R.id.numberPicker2 );


        np.setMinValue ( 0 );
        np.setMaxValue ( 59 );

        np2.setMinValue ( 0 );
        np2.setMaxValue ( 59 );

        np.setDescendantFocusability ( NumberPicker.FOCUS_BLOCK_DESCENDANTS );
        np2.setDescendantFocusability ( NumberPicker.FOCUS_BLOCK_DESCENDANTS );

        //np2.setOnValueChangedListener(this);


        np2.setOnValueChangedListener ( new NumberPicker.OnValueChangeListener () {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                playSound(mSoundLessId);

            }
        } );

        np.setOnValueChangedListener ( new NumberPicker.OnValueChangeListener () {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                playSound(mSoundLessId);

            }
        } );


        b1.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( getApplicationContext (), MainActivity.class );
                intent.putExtra ( "eggzBoilMinutes", np.getValue () );
                intent.putExtra ( "eggzBoilSeconds", np2.getValue () );

                startActivity ( intent );
                d.dismiss ();
            }
        } );


        d.show ();


    }

    /**
     * кнопка перехода в активити с таймером
     **/

    protected void startMainActivity(String eggzType) {
        Intent intent = new Intent ( this, MainActivity.class );
        intent.putExtra ( initString.getEggsType(), eggzType );
        startActivity ( intent );

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i ( "value is", "" + newVal );

    }

    @Override
    protected void onStart() {
        super.onStart ();

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setEnLanguage() {
        if(sharedPreferences.getAll().isEmpty()){
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString("languague","EN");
            editor.putString("soundOff","on");
            editor.apply();
        }
        if (Objects.equals(sharedPreferences.getString("languague", null), "EN")) {
            button_custom_picker_show.setBackgroundResource ( R.drawable.ic_button_custom_timer_picker_show_en );
            softEggzButton.setBackgroundResource ( R.drawable.ic_button_soft_en );
            hardEgggzButton.setBackgroundResource ( R.drawable.ic_button_hard_en );
            mediumEggzButton.setBackgroundResource ( R.drawable.ic_button_medium_en );
            titleView.setImageResource(R.drawable.ic_text_title_en);

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void playSound(int soundId) {
        if (Objects.equals(sharedPreferences.getString("soundOff", null), "on")) {
            mSoundPoolHelper.play(soundId);
        }
    }
}