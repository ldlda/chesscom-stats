package com.ldlda.chesscom_stats.ui.lessons;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.ui.lessons.data.Lesson;

import java.util.ArrayList;
import java.util.List;

// call this a normal class
public class LessonViewModel extends AndroidViewModel {
    public final LiveData<List<Lesson>> Cuh = new MutableLiveData<>(setupLessonList());

    public LessonViewModel(@NonNull Application application) {
        super(application);
    }

    private static List<Lesson> setupLessonList() {
        ArrayList<Lesson> lessonsList = new ArrayList<>();
        lessonsList.add(new Lesson("Lesson 1", R.string.lesson1_desc, "Beginner", R.drawable.lesson_1, 0xff81b64c));
        lessonsList.add(new Lesson("Lesson 2", R.string.lesson2_desc, "Intermidiate", R.drawable.lesson_2, 0xffa7c56a));
        lessonsList.add(new Lesson("Lesson 3", R.string.lesson3_desc, "Intermidiate", R.drawable.lesson_3, 0xffe42c3c));
        lessonsList.add(new Lesson("Lesson 4", R.string.lesson4_desc, "Advanced", R.drawable.lesson_4, 0xff4b4847));
        lessonsList.add(new Lesson("Lesson 5", R.string.lesson5_desc, "Advanced", R.drawable.lesson_5, 0xff39719c));
        return lessonsList;
    }
}
