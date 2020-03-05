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

public class CompletedTasksFragment extends Fragment {

    @BindView(R.id.completedTasksRecyclerView)
    RecyclerView completedTasksRecyclerView;
    @BindView(R.id.noTasksCompletedTextView)
    MaterialTextView noTasksCompletedTextView;
    private ArrayList<Task> completedTasks;

    public CompletedTasksFragment() {
        // Required empty public constructor
    }

    public CompletedTasksFragment(ArrayList<Task> completedTasks) {
        this.completedTasks = completedTasks;
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
        initViews();
        initRecyclerView();
        return view;
    }

    public void initViews() {
        if (completedTasks.isEmpty()) {
            completedTasksRecyclerView.setVisibility(View.GONE);
            noTasksCompletedTextView.setVisibility(View.VISIBLE);
        } else {
            completedTasksRecyclerView.setVisibility(View.VISIBLE);
            noTasksCompletedTextView.setVisibility(View.GONE);
        }
    }

    public void initRecyclerView() {
        completedTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        completedTasksRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter completedTasksAdapter = new TasksAdapter(completedTasks, getContext(), CompletedTasksFragment.this);
        completedTasksRecyclerView.setAdapter(completedTasksAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteArchiveCallback((TasksAdapter) completedTasksAdapter, "completed"));
        itemTouchHelper.attachToRecyclerView(completedTasksRecyclerView);

        //Hides/Shows FAB on scroll down/up
        completedTasksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        return completedTasksRecyclerView;
    }
}