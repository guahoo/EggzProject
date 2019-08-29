package com.guahoo.eggz.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SimplePagerAdapter extends PagerAdapter {


    private Context mContext;

    private String PREFERENCES;


    public SimplePagerAdapter(Context context) {
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        if (Objects.equals(sharedPreferences.getString("languague", null), "EN")) {
            ResourcesModel_en resources = ResourcesModel_en.values()[position];
            ViewGroup layout = (ViewGroup) inflater.inflate(
                    resources.getLayoutResourceId(), viewGroup, false);
            viewGroup.addView(layout);
            // получаем ресурсы (название и макет) соответствующий позиции в адаптере


            return layout;

            // инициализируем экран ViewPager'а в соответствии с позицией

        } else {
            ResourcesModel resources = ResourcesModel.values()[position];
            ViewGroup layout = (ViewGroup) inflater.inflate(
                    resources.getLayoutResourceId(), viewGroup, false);
            viewGroup.addView(layout);
            return layout;


            // инициализируем экран ViewPager'а в соответствии с позицией

        }


    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object view) {
        viewGroup.removeView((View) view);


    }

    @Override
    public int getCount() {
        // кличество элементов в адаптере соответствует
        // количеству значений в enum классе
        return ResourcesModel.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // получаем название соответствующее позиции в адаптере
        ResourcesModel customPagerEnum = ResourcesModel.values()[position];

        return mContext.getString(customPagerEnum.getTitleResourceId());
    }
}