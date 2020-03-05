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

public class ArchivedTasksFragment extends Fragment {

    @BindView(R.id.archivedTasksRecyclerView)
    RecyclerView archivedTasksRecyclerView;
    @BindView(R.id.noTasksArchivedTextView)
    MaterialTextView noTasksArchivedTextView;
    private ArrayList<Task> archivedTasks;

    public ArchivedTasksFragment() {
        // Required empty public constructor
    }

    public ArchivedTasksFragment(ArrayList<Task> archivedTasks) {
        this.archivedTasks = archivedTasks;
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
        initViews();
        initRecyclerView();
        return view;
    }

    public void initViews() {
        if (archivedTasks.isEmpty()) {
            archivedTasksRecyclerView.setVisibility(View.GONE);
            noTasksArchivedTextView.setVisibility(View.VISIBLE);
        } else {
            archivedTasksRecyclerView.setVisibility(View.VISIBLE);
            noTasksArchivedTextView.setVisibility(View.GONE);
        }
    }

    public void initRecyclerView() {
        archivedTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        archivedTasksRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter archivedTasksAdapter = new TasksAdapter(archivedTasks, getContext(), ArchivedTasksFragment.this);
        archivedTasksRecyclerView.setAdapter(archivedTasksAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteArchiveCallback((TasksAdapter) archivedTasksAdapter, "archived"));
        itemTouchHelper.attachToRecyclerView(archivedTasksRecyclerView);

        //Hides/Shows FAB on scroll down/up
        archivedTasksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        return archivedTasksRecyclerView;
    }
}