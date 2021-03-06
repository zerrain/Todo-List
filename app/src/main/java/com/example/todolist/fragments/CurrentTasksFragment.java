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
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentTasksFragment extends Fragment {

    @BindView(R.id.currentTasksRecyclerView)
    RecyclerView currentTasksRecyclerView;
    @BindView(R.id.noTasksAddedTextView)
    MaterialTextView noTasksAddedTextView;
    @BindView(R.id.allTasksCompletedTextView)
    MaterialTextView allTasksCompletedTextView;
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
        initViews();
        initRecyclerView();
        return view;
    }

    public void initViews() {
        if (currentTasks.isEmpty()) {
            currentTasksRecyclerView.setVisibility(View.GONE);
            if (((MainActivity) getContext()).getCompletedTasks().isEmpty()) {
                allTasksCompletedTextView.setVisibility(View.GONE);
                noTasksAddedTextView.setVisibility(View.VISIBLE);
            } else {
                allTasksCompletedTextView.setVisibility(View.VISIBLE);
                noTasksAddedTextView.setVisibility(View.GONE);
            }
        } else {
            currentTasksRecyclerView.setVisibility(View.VISIBLE);
            noTasksAddedTextView.setVisibility(View.GONE);
            allTasksCompletedTextView.setVisibility(View.GONE);
        }
    }

    public void initRecyclerView() {
        currentTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        currentTasksRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter currentTasksAdapter = new TasksAdapter(currentTasks, getContext(), CurrentTasksFragment.this);
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

    public RecyclerView getRecyclerView() {
        return currentTasksRecyclerView;
    }
}