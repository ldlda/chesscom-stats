package com.ldlda.chesscom_stats.ui.lessons.data;


import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class Lesson {

    private final String dataTitle;
    private final int dataDesc;
    private final String dataLevel;
    private final int dataImage;

    private final int color;


    public Lesson(String dataTitle, @StringRes int dataDesc, String dataLang, @DrawableRes int dataImage, int color) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLevel = dataLang;
        this.dataImage = dataImage;
        this.color = color;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public int getDataDesc() {
        return dataDesc;
    }

    public String getDataLang() {
        return dataLevel;
    }

    public int getDataImage() {
        return dataImage;
    }

    public int getColor() {
        return color;
    }
}
