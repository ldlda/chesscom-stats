package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView; // Correct import
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.adapter.LessonAdapter;
import com.ldlda.chesscom_stats.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonsFragment extends Fragment {

    RecyclerView recyclerView;
    List<Lesson> dataList;
    LessonAdapter adapter;
    SearchView searchView; // This will now correctly be the androidx version

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        // The cast is now correct because searchView is the androidx type
        searchView = view.findViewById(R.id.search);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        populateData();

        adapter = new LessonAdapter(requireContext(), dataList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void populateData() {
        dataList.add(new Lesson("Lesson 1", R.string.about, "Beginner", R.drawable.list_detail));
        dataList.add(new Lesson("RecyclerView", R.string.about, "Kotlin", R.drawable.list_detail));
        dataList.add(new Lesson("Date Picker", R.string.about, "Java", R.drawable.list_detail));
        dataList.add(new Lesson("EditText", R.string.about, "Kotlin", R.drawable.list_detail));
        dataList.add(new Lesson("Rating Bar", R.string.about, "Java", R.drawable.list_detail));
    }

    private void searchList(String text) {
        List<Lesson> dataSearchList = new ArrayList<>();
        for (Lesson data : dataList) {
            if (data.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(requireContext(), "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }
}
