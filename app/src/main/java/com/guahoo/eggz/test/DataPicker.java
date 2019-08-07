package com.guahoo.eggz.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.guahoo.eggz.R;

import java.util.ArrayList;

public class DataPicker extends View {

    public Context dataPickercontext = null; //Текущий Context
    private OnChangeValueListener mListener = null; //Слушатель нашего события смены значений
    public int nowTopPosition = 0; //Позиция скрола
    private int minTopPosition = 0; //Минимальная позиция скролла
    private int upMaxTopPosition = 0; //Максимальная позиция, в которую может уехать скрол вверх
    private int maxTopPosition = 0; //Максимальная позиция скрола внизу
    private int maxValueHeight = 0; //Максимальная высота значения
    private ArrayList <dpValuesSize> dpvalues = new ArrayList <dpValuesSize> (); //Значения
    private int canvasW = 0; //Текущая ширина холста
    private int canvasH = 0; //Текущая высота холста
    private int selectedvalueId = 0; //Идентификатор выбранного значения
    private boolean needAnimation = false; //Включать ли анимацию перемещения
    private int needPosition = 0; // Нуобходимая позиция
    public int valpadding = 30; //Отступ между значениями
    private int scrollspeed = 0; //Импульсная скорость скролла
    private boolean scrolltoup = false; //Движется ли скролл вверх
    private float dpDownY = 0; //Координаты клика по холсту с учетом смещения
    private float canvasDownY = 0; //Координаты точного клика по холсту
    private long actdownTime = 0; //Момент времени в который был совершен тап по холсту

    public DataPicker(Context context) {
        super ( context );
        dataPickercontext = context;
    }

    public DataPicker(Context context, AttributeSet attrs) {
        super ( context, attrs );
        dataPickercontext = context;
    }

    public DataPicker(Context context, AttributeSet attrs, int defStyle) {
        super ( context, attrs, defStyle );
        dataPickercontext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        canvasW = w;
        canvasH = h;
        maxValueHeight = ( canvasH - ( valpadding * 2 ) ) / 2;
        nowTopPosition = 0;
        super.onSizeChanged ( w, h, oldw, oldh );
    }

    private Handler dpHandler = new Handler ();

    public void setValues(final String[] newvalues) {
        if (canvasW == 0 || canvasH == 0) {
            dpHandler.postDelayed ( new Runnable () {
                @Override
                public void run() {
                    if (canvasW == 0 || canvasH == 0) {
                        dpHandler.postDelayed ( this, 100 );
                    } else {
                        dpvalues.clear ();
                        for (int i = 0; i < newvalues.length; i++) {
                            dpvalues.add ( new dpValuesSize ( newvalues[ i ], canvasW, canvasH ) );
                        }
                    }
                }
            }, 100 );
        }
        dpvalues.clear ();
        for (int i = 0; i < newvalues.length; i++) {
            dpvalues.add ( new dpValuesSize ( newvalues[ i ], canvasW, canvasH ) );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

//Проверяем. Если по холсту прошло событие нажатия, то запоминаем координаты по оси Y для нашего нажатия
        if (motionEvent.getAction () == MotionEvent.ACTION_DOWN) {
            canvasDownY = motionEvent.getY ();
            dpDownY = motionEvent.getY () - nowTopPosition;
            needAnimation = false;
            actdownTime = motionEvent.getEventTime ();

        }

//Во время скролла по нашему холсту, в переменную nowTopPosition мы записываем величину смещения пальца. Таким обзамо получаем величину, на которую нужно проскроллить наши значения.
        if (motionEvent.getAction () == MotionEvent.ACTION_MOVE) {
            if ((int) ( motionEvent.getY () - dpDownY ) > maxTopPosition) {
                nowTopPosition = maxTopPosition;
                return true;
            }
            if ((int) ( motionEvent.getY () - dpDownY ) < upMaxTopPosition) {
                nowTopPosition = upMaxTopPosition;
                return true;
            }
            nowTopPosition = (int) ( motionEvent.getY () - dpDownY );
        }

/*Когда палец был убран с холста - нам нужно вычислить к какому значению больше всего соответствует результат скролла, и выровнить значения так, чтобы они попадали строго под наш шаблон (Для этого имеем метод roundingValue().
Далее я дал себе волю немного поэкспериментировать с быстрым скроллингом и добавил переменную scrollspeed. Таким образом, чтобы при быстром перемещении пальца по холсту - у значений создавался запас хода, и значения продолжали скроллиться некоторое время.
*/
        if (motionEvent.getAction () == MotionEvent.ACTION_UP) {
            if (canvasDownY > motionEvent.getY ()) {
                scrolltoup = false;
            } else {
                scrolltoup = true;
            }

            if (( motionEvent.getEventTime () - actdownTime < 200 ) && ( Math.abs ( dpDownY - motionEvent.getY () ) > 100 )) {
                scrollspeed = (int) ( 1000 - ( motionEvent.getEventTime () - actdownTime ) );

            } else {
                scrollspeed = 0;
                roundingValue ();
            }
            needAnimation = true;

        }
        return true;
    }

    private void roundingValue() {
        //Вычисляем значение переменной needPosition, которая хранит в себе координаты скрола в выровненом значении.
        needPosition = ( ( ( nowTopPosition - maxTopPosition - ( maxValueHeight / 2 ) ) / ( maxValueHeight + valpadding ) ) ) * ( maxValueHeight + valpadding ) + maxTopPosition;
        //Вычисляем идентификатор значения, в котором произойдет остановка скрола
        selectedvalueId = Math.abs ( ( ( needPosition - valpadding - ( maxValueHeight / 2 ) ) / ( maxValueHeight + valpadding ) ) );
        // Сообщаем программисту о том, что некое значение было выбрано.
        onSelected ( selectedvalueId );
    }

    public void setOnChangeValueListener(OnChangeValueListener eventListener) {
        mListener = eventListener;
    }

    protected void onSelected(int selectedId) {
        if (mListener != null) {
            mListener.onEvent ( selectedId );
        }
    }

    //Возвращаем идентификатор выбранного значения
    public int getValueid() {
        try {
            return selectedvalueId;
        } catch (Exception e) {
        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw ( canvas );
        try {
            //Не будем ничего рисовать, если в Picker’е нет никаких значений
            if (dpvalues.size () == 0) {
                return;
            }
            //Определяем максимальную позицию скрола, в которую можно перемотать наши значения
            upMaxTopPosition = -( ( ( dpvalues.size () - 1 ) * ( maxValueHeight + valpadding ) ) );
            //Очищаем холст (Делаем его прозрачным)
            canvas.drawColor ( Color.argb ( 0, 255, 255, 255 ) );
            //Проверяем, нужно ли анимировать значения (например при перемотке значения были остановлены где-то посередине между двумя значениями)
            if (needAnimation) {
                if (scrollspeed > 0) {
                    scrollspeed -= 30;
                    if (scrolltoup) {
                        int currentPos = nowTopPosition + 30;
                        if (( currentPos ) > maxTopPosition) {
                            nowTopPosition = maxTopPosition;
                            scrollspeed = 0;
                            roundingValue ();
                        } else {
                            nowTopPosition = currentPos;
                        }
                    }
                    if (!scrolltoup) {
                        int currentPos = nowTopPosition - 30;
                        if (( currentPos ) < upMaxTopPosition) {
                            nowTopPosition = upMaxTopPosition;
                            scrollspeed = 0;
                            roundingValue ();
                        } else {
                            nowTopPosition = currentPos;
                        }
                    }
                    if (scrollspeed <= 0) {
                        roundingValue ();
                    }
                } else {
                    if (nowTopPosition > needPosition) {
                        nowTopPosition -= 20;
                        if (nowTopPosition < needPosition) {
                            nowTopPosition = needPosition;
                        }
                    }
                    if (nowTopPosition < needPosition) {
                        nowTopPosition += 20;
                        if (nowTopPosition > needPosition) {
                            nowTopPosition = needPosition;
                        }
                    }
                    if (nowTopPosition == needPosition) {
                        needAnimation = false;
                    }
                }
            }
            //Вставляем значения
            for (int i = 0; i < dpvalues.size (); i++) {
                try {
                    Paint paint = new Paint ();
                    paint.setColor ( dataPickercontext.getResources ().getColor ( R.color.datepickerText ) );
                    if (selectedvalueId == i) {
                        paint.setColor ( dataPickercontext.getResources ().getColor ( R.color.datepickerSelectedValue ) );
                        Paint shadowText = new Paint ();
                        shadowText.setColor ( dataPickercontext.getResources ().getColor ( R.color.datepickerSelectedValueShadow ) );
                        shadowText.setTextSize ( dpvalues.get ( i ).dpTextSize );

                        shadowText.setAntiAlias ( true );
                        canvas.drawText ( dpvalues.get ( i ).dpValue, ( canvasW / 2 ) - ( dpvalues.get ( i ).dpWidth / 2 ), ( ( maxValueHeight + valpadding ) * i ) + ( valpadding + maxValueHeight ) + ( dpvalues.get ( i ).dpHeight / 2 ) + nowTopPosition + 2, shadowText );
                    }
                    paint.setTextSize ( dpvalues.get ( i ).dpTextSize );
                    paint.setAntiAlias ( true );

                    canvas.drawText ( dpvalues.get ( i ).dpValue, ( canvasW / 2 ) - ( dpvalues.get ( i ).dpWidth / 2 ), ( ( maxValueHeight + valpadding ) * i ) + ( valpadding + maxValueHeight ) + ( dpvalues.get ( i ).dpHeight / 2 ) + nowTopPosition, paint );
                } catch (Exception e) {
                }
            }


            Paint framePaint = new Paint ();
            framePaint.setShader ( new LinearGradient ( 0, 0, 0, getHeight () / 5, dataPickercontext.getResources ().getColor ( R.color.datapickerGradientStart ), Color.TRANSPARENT, Shader.TileMode.CLAMP ) );
            canvas.drawPaint ( framePaint );
            framePaint.setShader ( new LinearGradient ( 0, getHeight (), 0, getHeight () - getHeight () / 5, dataPickercontext.getResources ().getColor ( R.color.datapickerGradientStart ), Color.TRANSPARENT, Shader.TileMode.CLAMP ) );
            canvas.drawPaint ( framePaint );
            //Рисуем полоску веделенного текста
            Path pathSelect = new Path ();
            pathSelect.moveTo ( 0, canvasH / 2 - maxValueHeight / 2 - valpadding / 2 );
            pathSelect.lineTo ( canvasW, canvasH / 2 - maxValueHeight / 2 - valpadding / 2 );
            pathSelect.lineTo ( canvasW, canvasH / 2 );
            pathSelect.lineTo ( 0, canvasH / 2 );
            pathSelect.lineTo ( 0, canvasH / 2 - maxValueHeight / 2 );
            Paint pathSelectPaint = new Paint ();
            pathSelectPaint.setShader ( new LinearGradient ( 0, 0, 0, maxValueHeight / 2, dataPickercontext.getResources ().getColor ( R.color.datapickerSelectedValueeLineG1 ), dataPickercontext.getResources ().getColor ( R.color.datapickerSelectedValueeLineG2 ), Shader.TileMode.CLAMP ) );
            canvas.drawPath ( pathSelect, pathSelectPaint );

            pathSelect = new Path ();
            pathSelect.moveTo ( 0, canvasH / 2 );
            pathSelect.lineTo ( canvasW, canvasH / 2 );
            pathSelect.lineTo ( canvasW, canvasH / 2 + maxValueHeight / 2 + valpadding / 2 );
            pathSelect.lineTo ( 0, canvasH / 2 + maxValueHeight / 2 + valpadding / 2 );
            pathSelect.lineTo ( 0, canvasH / 2 );
            pathSelectPaint = new Paint ();
            pathSelectPaint.setShader ( new LinearGradient ( 0, 0, 0, maxValueHeight / 2, dataPickercontext.getResources ().getColor ( R.color.datapickerSelectedValueeLineG3 ), dataPickercontext.getResources ().getColor ( R.color.datapickerSelectedValueeLineG4 ), Shader.TileMode.CLAMP ) );
            canvas.drawPath ( pathSelect, pathSelectPaint );

            Paint selValLightBorder = new Paint ();
            Paint selValTopBorder = new Paint ();
            Paint selValBottomBorder = new Paint ();
            selValLightBorder.setColor ( dataPickercontext.getResources ().getColor ( R.color.datapicketSelectedValueBorder ) );
            selValTopBorder.setColor ( dataPickercontext.getResources ().getColor ( R.color.datapicketSelectedBorderTop ) );
            selValBottomBorder.setColor ( dataPickercontext.getResources ().getColor ( R.color.datapicketSelectedBorderBttom ) );
            canvas.drawLine ( 0, canvasH / 2 - maxValueHeight / 2 - valpadding / 2, canvasW, canvasH / 2 - maxValueHeight / 2 - valpadding / 2, selValLightBorder );
            canvas.drawLine ( 0, canvasH / 2 - maxValueHeight / 2 - valpadding / 2 + 1, canvasW, canvasH / 2 - maxValueHeight / 2 - valpadding / 2 + 1, selValTopBorder );
            canvas.drawLine ( 0, canvasH / 2 + maxValueHeight / 2 + valpadding / 2, canvasW, canvasH / 2 + maxValueHeight / 2 + valpadding / 2, selValLightBorder );
            canvas.drawLine ( 0, canvasH / 2 + maxValueHeight / 2 + valpadding / 2 - 1, canvasW, canvasH / 2 + maxValueHeight / 2 + valpadding / 2 - 1, selValBottomBorder );

            canvas.drawLine ( 0, canvasH / 2 - maxValueHeight / 2 - valpadding / 2, 0, canvasH / 2 + maxValueHeight / 2 + valpadding / 2, selValLightBorder );
            canvas.drawLine ( 1, canvasH / 2 - maxValueHeight / 2 - valpadding / 2, 1, canvasH / 2 + maxValueHeight / 2 + valpadding / 2, selValLightBorder );
            canvas.drawLine ( canvasW - 1, canvasH / 2 - maxValueHeight / 2 - valpadding / 2, canvasW - 1, canvasH / 2 + maxValueHeight / 2 + valpadding / 2, selValLightBorder );
            canvas.drawLine ( canvasW - 2, canvasH / 2 - maxValueHeight / 2 - valpadding / 2, canvasW - 2, canvasH / 2 + maxValueHeight / 2 + valpadding / 2, selValLightBorder );
            canvas.drawLine ( canvasW, canvasH / 2 - maxValueHeight / 2 - valpadding / 2, canvasW, canvasH / 2 + maxValueHeight / 2 + valpadding / 2, selValLightBorder );

            //Рисуем выделенный текст с "тенью"
            Paint selectedTextPaint = new Paint ();
            selectedTextPaint.setColor ( dataPickercontext.getResources ().getColor ( R.color.datepickerSelectedValue ) );
            Paint shadowText = new Paint ();
            shadowText.setColor ( dataPickercontext.getResources ().getColor ( R.color.datepickerSelectedValueShadow ) );
            shadowText.setTextSize ( dpvalues.get ( selectedvalueId ).dpTextSize );
            shadowText.setAntiAlias ( true );
            canvas.drawText ( dpvalues.get ( selectedvalueId ).dpValue, ( canvasW / 2 ) - ( dpvalues.get ( selectedvalueId ).dpWidth / 2 ), ( ( maxValueHeight + valpadding ) * selectedvalueId ) + ( valpadding + maxValueHeight ) + ( dpvalues.get ( selectedvalueId ).dpHeight / 2 ) + nowTopPosition + 2, shadowText );
            selectedTextPaint.setTextSize ( dpvalues.get ( selectedvalueId ).dpTextSize );
            selectedTextPaint.setAntiAlias ( true );
            canvas.drawText ( dpvalues.get ( selectedvalueId ).dpValue, ( canvasW / 2 ) - ( dpvalues.get ( selectedvalueId ).dpWidth / 2 ), ( ( maxValueHeight + valpadding ) * selectedvalueId ) + ( valpadding + maxValueHeight ) + ( dpvalues.get ( selectedvalueId ).dpHeight / 2 ) + nowTopPosition, selectedTextPaint );
        } catch (Exception e) {
            e.printStackTrace ();
        }

        //Перерисовка канваса или FPS. количество кадров прорисовки в секунду
        this.postInvalidateDelayed ( 1000 / 60 );

    }


    interface OnChangeValueListener {
        public void onEvent(int valueId);
    }


    class dpValuesSize {
        public int dpWidth = 0; //Ширина нашего текста
        public int dpHeight = 0; //Высота нашего текста
        public String dpValue = ""; //Значения текста
        public int dpTextSize = 0; // Размер шрифта
        public int valpadding = 30; //Отступ между значениями
        public int valinnerLeftpadding = 20; //Отступ по краям у значения

        /*
    Нам необходимо подогнать размер шрифта таким образом, чтобы значение максимально плотно влезало в доверенное ему поле. Грубо говоря - текст должен быть такого размера, чтобы полностью вмещался в наш View, но при этом не вылазил бы за его границы.

    Решение на мой взгляд не совсем элегантное. В цикле мы увеличиваем размер шрифта до тех пор, пока он не будет больше нашего поля. Как только размер превышен - останавливаем цикл и берем предыдущее значение.
    Более элегантного алгоритма я не придумал, поэтому буду рад любым идеям и комментариям к данному алгоритму
    */
        public dpValuesSize(String val, int canvasW, int canvasH) {
            try {
                int maxTextHeight = ( canvasH - ( valpadding * 2 ) ) / 2;
                boolean sizeOK = false;
                dpValue = val;
                while (!sizeOK) {
                    Rect textBounds = new Rect ();
                    Paint textPaint = new Paint ();
                    dpTextSize++;
                    textPaint.setTextSize ( dpTextSize );

                    textPaint.getTextBounds ( val, 0, val.length (), textBounds );
                    if (textBounds.width () <= canvasW - ( valinnerLeftpadding * 2 ) && textBounds.height () <= maxTextHeight) {
                        dpWidth = textBounds.width ();
                        dpHeight = textBounds.height ();
                    } else {
                        sizeOK = true;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
    }
}
