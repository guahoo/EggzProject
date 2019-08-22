package com.guahoo.eggz.Utility;

import com.guahoo.eggz.R;

public enum ResourcesModel_en {

    // создаем 3 перечисления с тайтлом и макетом
    // для удобной работы в адаптере
    FIRST_SCREEN(1, R.layout.resipe1_en),
    SECOND_SCREEN( 2,R.layout.resipe3_en),
    THIRD_SCREEN(3,R.layout.resipe2_en),
    FOURTH_SCREEN(4,R.layout.resipe4_en),
    FIFTH_SCREEN(5,R.layout.resipe5_en),
    SIXTH_SCREEN(6,R.layout.resipe6_en);

    private int mTitleResourceId;
    private int mLayoutResourceId;
    private String mResourseTitle;

    ResourcesModel_en(int titleResId, int layoutResId) {
        mTitleResourceId = titleResId;
        mLayoutResourceId = layoutResId;
    }

    public int getTitleResourceId() {
        return mTitleResourceId;
    }

    public int getLayoutResourceId() {
        return mLayoutResourceId;
    }
}