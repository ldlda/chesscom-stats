package com.ldlda.chesscom_stats;

public class Lesson {

    private String dataTitle;
    private int dataDesc;
    private String dataLevel;
    private int dataImage;

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
