package com.example.todolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.SwipeToDeleteArchiveCallback;
import com.example.todolist.Task;
import com.example.todolist.TasksAdapter;
import com.example.todolist.activities.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentTasksFragment extends Fragment {

    @BindView(R.id.currentTasksRecyclerView)
    RecyclerView currentTasksRecyclerView;
    private ArrayList<Task> currentTasks;

    public CurrentTasksFragment() {
        // Required empty public constructor
    }

    public CurrentTasksFragment(ArrayList<Task> currentTasks) {
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteArchiveCallback((TasksAdapter) currentTasksAdapter, "current"));
        itemTouchHelper.attachToRecyclerView(currentTasksRecyclerView);

        //Hides/Shows FAB on scroll down/up
        currentTasksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    ((MainActivity) getActivity()).hideFAB();
                else if (dy < 0)
                    ((MainActivity) getActivity()).showFAB();
            }
        });
    }
}