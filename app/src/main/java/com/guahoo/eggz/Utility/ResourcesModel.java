package com.guahoo.eggz.Utility;

import com.guahoo.eggz.R;

public enum ResourcesModel {

    // создаем 3 перечисления с тайтлом и макетом
    // для удобной работы в адаптере
    FIRST_SCREEN(1, R.layout.resipe1),
    SECOND_SCREEN( 2,R.layout.resipe3),
    THIRD_SCREEN(3,R.layout.resipe2),
    FOURTH_SCREEN(4,R.layout.resipe4),
    FIFTH_SCREEN(5,R.layout.resipe5),
    SIXTH_SCREEN(6,R.layout.resipe6);

    private int mTitleResourceId;
    private int mLayoutResourceId;
    private String mResourseTitle;

    ResourcesModel(int titleResId, int layoutResId) {
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