package com.guahoo.eggz.Activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guahoo.eggz.Utility.InitString;
import com.guahoo.eggz.Utility.NotificationTimerBar;
import com.guahoo.eggz.Utility.PlaySound;
import com.guahoo.eggz.R;

import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    ImageView eggzTitle;
    TextView timeView;
    private CountDownTimer timer;
    boolean mTimerRunning;
    public long START_TIME_IN_MILLIS;



    public static long mtimeleftminutes;
    ProgressBar mProgress;
    Button crossButton;
    ImageView eggzFinal;
    Button finishButton;
    Vibrator vibrator;
    NotificationTimerBar notificationTimerBar;
    NotificationManager notificationManager;

    Button startButton;
    Button resetButton;
    ImageView yellowCircle, timerView;
    SharedPreferences sharedPreferences;
    InitString initString;
    String PREFERENCES;
    String NOTIF_CHANNEL_ID;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LOCKED );
        requestWindowFeature ( Window.FEATURE_NO_TITLE );
   //     getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        if (getSupportActionBar () != null) {
            getSupportActionBar ().setDisplayShowTitleEnabled ( false );
            getSupportActionBar ().hide ();
        }
        super.onCreate ( savedInstanceState );

        setContentView ( R.layout.activity_main );


        timeView = findViewById ( R.id.timeText );
        startButton = findViewById ( R.id.buttonStart );
        resetButton = findViewById ( R.id.resetButton );
        mProgress = findViewById ( R.id.progress );
        crossButton = findViewById ( R.id.backButton );
        eggzTitle = findViewById ( R.id.eggzType );
        eggzFinal = findViewById ( R.id.eggzFinal );
        finishButton = findViewById ( R.id.finishButton );
        eggzFinal.setVisibility ( View.INVISIBLE );
        finishButton.setVisibility ( View.INVISIBLE );
        yellowCircle = findViewById ( R.id.yellow_circle );
        timerView = findViewById ( R.id.timerView );

        vibrator = (Vibrator) getSystemService ( Context.VIBRATOR_SERVICE );
        initString = new InitString();
        notificationTimerBar=new NotificationTimerBar(this);
        sharedPreferences = getApplicationContext ().getSharedPreferences (PREFERENCES, MODE_PRIVATE );
        stateSettings ();
        setEnLanguage ();


        mProgress.setMax ( (int) START_TIME_IN_MILLIS );
        mProgress.setProgress ( (int) START_TIME_IN_MILLIS );
        resetButton.setEnabled ( false );
        updateTimeView ();
        notificationTimerBar.updateNotification();
        notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);



        finishButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( getApplicationContext (), StartActivity.class );
                vibrator.cancel ();
                startActivity ( intent );
            }
        } );


        crossButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    timer.cancel ();
                }
                Intent intent = new Intent ( getApplicationContext (), StartActivity.class );
                startActivity ( intent );
            }
        } );


        startButton.setOnClickListener ( new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer ();
                } else {
                    startTimer ();
                    startButton.setBackgroundResource ( R.drawable.buttonpauseselector );
                    if (Objects.equals(sharedPreferences.getString(initString.getLanguage(), null), "EN")) {
                        startButton.setBackgroundResource ( R.drawable.button_pause_selector_en );
                    }
                }

            }
        } );
        resetButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                resetTimer ();
            }
        } );

    }


    protected void updateTimeView() {
        int minutes = (int) mtimeleftminutes / 1000 / 60;
        int seconds = (int) mtimeleftminutes / 1000 % 60;
        String timeLeftFormatted = String.format ( Locale.getDefault (), "%02d:%02d", minutes, seconds );
        timeView.setText ( timeLeftFormatted );


    }

    protected void startTimer() {
        timer = new CountDownTimer ( mtimeleftminutes, 1000 ) {
            @Override
            public void onTick(long millisUntilFinished) {
                mtimeleftminutes = millisUntilFinished;
                mProgress.setProgress ( (int) millisUntilFinished );
                updateTimeView ();
                notificationTimerBar.updateNotification();
            }


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFinish() {
                timeView.setVisibility ( View.INVISIBLE );
                mProgress.setVisibility ( View.INVISIBLE );
                resetButton.setVisibility ( View.INVISIBLE );
                startButton.setVisibility ( View.INVISIBLE );
                eggzTitle.setVisibility ( View.INVISIBLE );
                eggzFinal.setVisibility ( View.VISIBLE );
                crossButton.setVisibility ( View.INVISIBLE );
                finishButton.setVisibility ( View.VISIBLE );
                timerView.setVisibility ( View.INVISIBLE );
                yellowCircle.setVisibility ( View.INVISIBLE );
                vibrator.vibrate ( 1000 );
                PlaySound playSound = new PlaySound ();
                playSound.playSound ( sharedPreferences, getApplicationContext (), R.raw.final_sound );
                timer.cancel ();
                notificationManager.cancelAll();
            }
        }.start ();


        mTimerRunning = true;
        resetButton.setEnabled ( true );
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pauseTimer() {
        timer.cancel ();
        mTimerRunning = false;
        startButton.setBackgroundResource ( R.drawable.buttonresumeselector );
        if (Objects.equals(sharedPreferences.getString(initString.getLanguage(), null), "EN")) {
            startButton.setBackgroundResource ( R.drawable.button_resume_selector_en );
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void resetTimer() {
        mtimeleftminutes = START_TIME_IN_MILLIS;
        updateTimeView ();
        startButton.setBackgroundResource ( R.drawable.buttonstartselector );
        if (Objects.equals(sharedPreferences.getString(initString.getLanguage(), null), "EN")) {
            startButton.setBackgroundResource ( R.drawable.button_start_selector_en );
        }

        timer.cancel ();
        mTimerRunning = false;
        mProgress.setProgress ( (int) START_TIME_IN_MILLIS );
    }


    public void setMtimeleftminutes(long mtimeleftminutes) {
        MainActivity.mtimeleftminutes = mtimeleftminutes;
        START_TIME_IN_MILLIS = mtimeleftminutes;
    }


    /**
     * setTimerMinutes
     **/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void stateSettings() {
        Intent intent = getIntent ();
        if (intent.hasExtra ( initString.getEggsType() )) {
            Bundle extras = getIntent ().getExtras ();
            assert extras != null;
            switch (Objects.requireNonNull(extras.getString(initString.getEggsType()))) {
                case "hard":
                    eggzTitle.setImageResource(R.drawable.ic_text_hard);
                    eggzFinal.setImageResource(R.drawable.ic_eggz_final_hard);
                    setLanguage(eggzTitle, eggzFinal, R.drawable.ic_text_hard_en, R.drawable.ic_eggz_final_hard_en);

                    setMtimeleftminutes(480000);
                    break;
                case "medium":
                    eggzTitle.setImageResource(R.drawable.ic_text_medium);
                    eggzFinal.setImageResource(R.drawable.ic_eggz_final_medium);
                    setLanguage(eggzTitle, eggzFinal, R.drawable.ic_text_medium_en, R.drawable.ic_eggz_final_medium_en);

                    setMtimeleftminutes(300000);
                    break;

                case "soft":
                    eggzTitle.setImageResource(R.drawable.ic_text_soft);
                    eggzFinal.setImageResource(R.drawable.ic_eggz_final_soft);
                    setLanguage(eggzTitle, eggzFinal, R.drawable.ic_text_soft_en, R.drawable.ic_eggz_final_soft_en);


                    setMtimeleftminutes(180000);
                    break;
            }

        } else {
            Bundle extras = getIntent ().getExtras ();
            assert extras != null;


            int customeggzBoilTime = ( extras.getInt ( "eggzBoilMinutes" ) * 1000 * 60 ) +
                    ( extras.getInt ( "eggzBoilSeconds" ) * 1000 );

            if (customeggzBoilTime < 1) {
                customeggzBoilTime = 1;
            }
            setMtimeleftminutes ( customeggzBoilTime );

            if (customeggzBoilTime < 300000) {
                eggzTitle.setImageResource ( R.drawable.ic_text_soft );
                eggzFinal.setImageResource ( R.drawable.ic_eggz_final_soft );
                setLanguage(eggzTitle,eggzFinal,R.drawable.ic_text_soft_en,R.drawable.ic_eggz_final_soft_en);
            } else if (customeggzBoilTime >= 300000 & customeggzBoilTime < 480000) {

                eggzTitle.setImageResource ( R.drawable.ic_text_medium );
                eggzFinal.setImageResource ( R.drawable.ic_eggz_final_medium );
                setLanguage(eggzTitle,eggzFinal,R.drawable.ic_text_medium_en,R.drawable.ic_eggz_final_medium_en);
            } else if (customeggzBoilTime >= 480000) {



                eggzTitle.setImageResource ( R.drawable.ic_text_hard );
                eggzFinal.setImageResource ( R.drawable.ic_eggz_final_hard );
                setLanguage(eggzTitle,eggzFinal,R.drawable.ic_text_hard_en,R.drawable.ic_eggz_final_hard_en);
            }
        }
    }

    protected void setEnLanguage() {
        if (sharedPreferences.getString ( initString.getLanguage(), null ).equals ( "EN" )) {
            finishButton.setBackgroundResource ( R.drawable.ic_button_done_en );
            resetButton.setBackgroundResource ( R.drawable.ic_button_reset_en );
            startButton.setBackgroundResource ( R.drawable.button_start_selector_en );
            timerView.setImageResource ( R.drawable.ic_text_timer_en );





            if (eggzTitle.getDrawable ().getConstantState ()
                    == getResources ().getDrawable ( R.drawable.ic_text_soft ).getConstantState ()) {
                eggzTitle.setImageResource ( R.drawable.ic_text_soft_en );
                eggzFinal.setImageResource(R.drawable.ic_eggz_final_soft_en);
            } else if (eggzTitle.getDrawable ().getConstantState ()
                    == getResources ().getDrawable ( R.drawable.ic_text_medium ).getConstantState ()) {
                eggzTitle.setImageResource ( R.drawable.ic_text_medium_en );
                eggzFinal.setImageResource(R.drawable.ic_eggz_final_medium_en);
            } else if (eggzTitle.getDrawable ().getConstantState ()
                    == getResources ().getDrawable ( R.drawable.ic_text_hard ).getConstantState ()) {
                eggzTitle.setImageResource ( R.drawable.ic_text_hard_en );
                eggzFinal.setImageResource(R.drawable.ic_eggz_final_hard_en);

            }
        }


    }
    void setLanguage(ImageView imageViewTitle,ImageView imageViewFinal, int t,int f){
        if (sharedPreferences.getString ( initString.getLanguage(), null ).equals ( "EN" )) {
            imageViewTitle.setImageResource(t);
            imageViewFinal.setImageResource(f);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel ();
        mTimerRunning = false;
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }
}

