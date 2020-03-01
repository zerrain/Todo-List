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

public class ArchivedTasksFragment extends Fragment {

    @BindView(R.id.archivedTasksRecyclerView)
    RecyclerView archivedTasksRecyclerView;
    private ArrayList<String> archivedTasks;

    public ArchivedTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archived_tasks, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        loadArchivedTasks();
        archivedTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        archivedTasksRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter archivedTasksAdapter = new TasksAdapter(archivedTasks, getContext());
        archivedTasksRecyclerView.setAdapter(archivedTasksAdapter);
    }

    public void loadArchivedTasks() {
        File tasksFile = new File(getContext().getFilesDir(), "archivedTasks.txt");
        try {
            archivedTasks = new ArrayList<>(FileUtils.readLines(tasksFile));
        } catch (Exception e) {
            e.printStackTrace();
            archivedTasks = new ArrayList<>();
        }
    }

    public void saveArchivedTasks() {
        File tasksFile = new File(getContext().getFilesDir(), "archivedTasks.txt");
        try {
            FileUtils.writeLines(tasksFile, archivedTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}