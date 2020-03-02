package com.example.todolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.TasksAdapter;
import com.example.todolist.activities.MainActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentTasksFragment extends Fragment {

    @BindView(R.id.currentTasksRecyclerView)
    RecyclerView currentTasksRecyclerView;
    private ArrayList<String> currentTasks;

    public CurrentTasksFragment() {
        // Required empty public constructor
    }

    public CurrentTasksFragment(ArrayList<String> currentTasks) {
        this.currentTasks = currentTasks;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_tasks, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        currentTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        currentTasksRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter currentTasksAdapter = new TasksAdapter(currentTasks, getContext());
        currentTasksRecyclerView.setAdapter(currentTasksAdapter);

        //Hides/Shows FAB on scroll down/up
        currentTasksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    ((MainActivity)getActivity()).hideFAB();
                else if (dy < 0)
                    ((MainActivity)getActivity()).showFAB();
            }
        });
    }
}