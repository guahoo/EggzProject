package com.guahoo.eggz.Utility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.guahoo.eggz.Activity.StartActivity;
import com.guahoo.eggz.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public  class Dialog_menu {
    final Button buttonsoundOn;
    final Button buttonsoundOff;

    final Button russian_Languegue_Button;
    final Button english_Languegue_Button;
    final ImageView yellow_Langugue_Background;
     SharedPreferences sharedPreferences;
    boolean languague_menu_is_active;
    Context context;
     ImageView menuTitle;

     Button recipeButton;
    Button languague_Button;


    SharedPreferences PREFERENSES;

    public Dialog_menu(Button buttonsoundOn, Button buttonsoundOff,
                       Button languague_Button, Button russian_Languegue_Button, Button english_Languegue_Button,
                       ImageView yellow_Langugue_Background, SharedPreferences sharedPreferences, boolean languague_menu_is_active, Context context){
        this.buttonsoundOn = buttonsoundOn;
        this.buttonsoundOff = buttonsoundOff;
        this.languague_Button = languague_Button;
        this.russian_Languegue_Button = russian_Languegue_Button;
        this.english_Languegue_Button = english_Languegue_Button;
        this.yellow_Langugue_Background = yellow_Langugue_Background;
        this.sharedPreferences = sharedPreferences;
        this.languague_menu_is_active = languague_menu_is_active;
        this.context = context;


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    public void showMenuDialog(){
        final Dialog d = new Dialog(context);
        d.requestWindowFeature( Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable (android.graphics.Color.TRANSPARENT));
        Window window = d.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        d.setContentView( R.layout.menu_dialog);



        final Button buttonsoundOn;
        final Button buttonsoundOff;

        final Button russian_Languegue_Button;
        final Button english_Languegue_Button;
        final ImageView yellow_Langugue_Background;
        final SharedPreferences sharedPreferences;


        buttonsoundOff = d.findViewById ( R.id.bell_off_button );
        buttonsoundOn = d.findViewById ( R.id.bell_on_button );
        yellow_Langugue_Background = d.findViewById ( R.id.languague_yellow_background );
        languague_Button = d.findViewById ( R.id.languague_button );
        russian_Languegue_Button= d.findViewById ( R.id.russian_button );
        english_Languegue_Button = d.findViewById ( R.id.english_button );
        menuTitle = d.findViewById ( R.id.menu_title );
        recipeButton = d.findViewById ( R.id.resipes_button );
        languague_menu_is_active=false;
        sharedPreferences = context.getSharedPreferences ( "soundOff",MODE_PRIVATE );
        final SharedPreferences.Editor editor = sharedPreferences.edit ();

        if (sharedPreferences.getString ( "soundOff",null )==null){
            editor.putString ( "soundOff","null" );
            editor.apply ();
        }

        if (Objects.requireNonNull ( sharedPreferences.getString ( "soundOff", null ) ).equals ( "off" )){
            buttonsoundOff.setBackgroundResource ( R.drawable.ic_off);
            buttonsoundOn.setBackgroundResource ( R.drawable.ic_on );
        }

        if (sharedPreferences.getString ( "languague", null ).equals ( "EN" )) {
            menuTitle.setImageResource ( R.drawable.ic_text_menu_en );
            recipeButton.setBackgroundResource ( R.drawable.ic_button_resipe_en );
            languague_Button.setBackgroundResource ( R.drawable.ic_button_language_en );

        }


/**кнопка включени языкового меню**/

        languague_Button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                if (!languague_menu_is_active) {
                    buttonsoundOff.setVisibility ( View.INVISIBLE );
                    buttonsoundOn.setVisibility ( View.INVISIBLE );
                    yellow_Langugue_Background.setVisibility ( View.VISIBLE );
                    english_Languegue_Button.setVisibility ( View.VISIBLE );
                    russian_Languegue_Button.setVisibility ( View.VISIBLE );
                    if (Objects.requireNonNull ( sharedPreferences.getString ( "languague", "EN" ) ).equals ( "EN" )){
                        english_Languegue_Button.setBackgroundResource ( R.drawable.ic_english_black);
                        russian_Languegue_Button.setBackgroundResource ( R.drawable.ic_russian_yellow );

                    } else {
                        english_Languegue_Button.setBackgroundResource ( R.drawable.ic_english_yellow );
                        russian_Languegue_Button.setBackgroundResource ( R.drawable.ic_russian_black );
                    }
                    languague_menu_is_active = true;
                }

                else if (languague_menu_is_active){
                    buttonsoundOff.setVisibility ( View.VISIBLE );
                    buttonsoundOn.setVisibility ( View.VISIBLE);
                    yellow_Langugue_Background.setVisibility ( View.INVISIBLE );
                    english_Languegue_Button.setVisibility ( View.INVISIBLE );
                    russian_Languegue_Button.setVisibility ( View.INVISIBLE );
                    languague_menu_is_active = false;

                }
            }

        } );

        /**кнопки выбора языка**/

        russian_Languegue_Button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                english_Languegue_Button.setBackgroundResource ( R.drawable.ic_english_yellow);
                russian_Languegue_Button.setBackgroundResource ( R.drawable.ic_russian_black );
                editor.putString ( "languague", "RU" );
                editor.apply ();
                setRuLanguage();


            }
        } );

        english_Languegue_Button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                english_Languegue_Button.setBackgroundResource ( R.drawable.ic_english_black);
                russian_Languegue_Button.setBackgroundResource ( R.drawable.ic_russian_yellow);
                editor.putString ( "languague", "EN" );
                editor.apply ();
                setEnLanguage();


            }
        } );

        buttonsoundOn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                editor.putString ( "soundOff","on" );
                editor.apply ();

                buttonsoundOn.setBackgroundResource ( R.drawable.ic_on_ye);
                buttonsoundOff.setBackgroundResource ( R.drawable.ic_off_black);

            }
        } );

        buttonsoundOff.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                editor.putString ( "soundOff", "off"  );
                editor.apply ();
                buttonsoundOn.setBackgroundResource ( R.drawable.ic_on);
                buttonsoundOff.setBackgroundResource ( R.drawable.ic_off );
            }
        } );

        Button crossButton = d.findViewById ( R.id.crossButton_menu_dialog );
        crossButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( context, StartActivity.class );
                context.startActivity ( intent );
            }
        } );

        d.show ();


        recipeButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, RecipeActivity.class );
                context.startActivity (intent);
            }
        } ) ;

        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                context.startActivity(new Intent(context,StartActivity.class));
            }
        });



    }
    public void setEnLanguage() {
        if (sharedPreferences.getString ( "languague", null ).equals ( "EN" )) {
            recipeButton.setBackgroundResource(R.drawable.ic_button_resipe_en);
            languague_Button.setBackgroundResource(R.drawable.ic_button_language_en);
            menuTitle.setImageResource(R.drawable.ic_text_menu_en);
        }
    }

    public void setRuLanguage() {
        if (sharedPreferences.getString ( "languague", null ).equals ( "RU" )) {
            recipeButton.setBackgroundResource(R.drawable.ic_recept);
            languague_Button.setBackgroundResource(R.drawable.ic_language);
            menuTitle.setImageResource(R.drawable.ic_text_menu);
        }
    }


}
