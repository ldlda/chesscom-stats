package com.ldlda.chesscom_stats.ui.lessons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.ui.lessons.data.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonsFragment extends Fragment {

    RecyclerView recyclerView;
    List<Lesson> dataList;
    LessonsAdapter adapter;
    SearchView searchView; // This will now correctly be the androidx version

    LessonViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);
        dataList = viewModel.Cuh.getValue();

        adapter = new LessonsAdapter(requireContext(), dataList, this::showLessonContent);
        recyclerView.setAdapter(adapter);

        return view;
    }

    // why duplicate
    // this is not the same view WE NEED TO VIEWMODEL AGAIN


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

    public void showLessonContent(Bundle args) {
        // Get lesson index from title
        String title = args.getString("Title");
        int startIndex = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getDataTitle().equals(title)) {
                startIndex = i;
                break;
            }
        }

        // Navigate to lesson pager container using Navigation Component
        Bundle bundle = new Bundle();
        bundle.putInt("startIndex", startIndex);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_lessons_to_pager, bundle);
    }
}
