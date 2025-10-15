package com.ldlda.chesscom_stats.ui.lessons.data;


import kotlinx.parcelize.Parcelize;

@Parcelize
public class Lesson {

    private final String dataTitle;
    private final int dataDesc;
    private final String dataLevel;
    private final int dataImage;

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

    public Lesson(String dataTitle, int dataDesc, String dataLang, int dataImage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLevel = dataLang;
        this.dataImage = dataImage;
    }
}
