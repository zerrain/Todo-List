package com.example.todolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.TasksAdapter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedTasksFragment extends Fragment {

    @BindView(R.id.completedTasksRecyclerView)
    RecyclerView completedTasksRecyclerView;
    private ArrayList<String> completedTasks;

    public CompletedTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_tasks, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        loadCompletedTasks();
        completedTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        completedTasksRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter completedTasksAdapter = new TasksAdapter(completedTasks, getContext());
        completedTasksRecyclerView.setAdapter(completedTasksAdapter);
    }

    public void loadCompletedTasks() {
        File tasksFile = new File(getContext().getFilesDir(), "completedTasks.txt");
        try {
            completedTasks = new ArrayList<>(FileUtils.readLines(tasksFile));
        } catch (Exception e) {
            e.printStackTrace();
            completedTasks = new ArrayList<>();
        }
    }

    public void saveCompletedTasks() {
        File tasksFile = new File(getContext().getFilesDir(), "completedTasks.txt");
        try {
            FileUtils.writeLines(tasksFile, completedTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}