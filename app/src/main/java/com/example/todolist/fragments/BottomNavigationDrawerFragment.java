package com.example.todolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.R;
import com.example.todolist.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    NavigationView bottomNavView;
    View view;

    public BottomNavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        initNavView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initNavView(View view) {
        bottomNavView = view.findViewById(R.id.bottomNavView);
        bottomNavView.getMenu().getItem(0).setChecked(true);

        bottomNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.tasksCurrentNavItem:
                        switchToCurrentTasks();
                        break;
                    case R.id.tasksCompletedNavItem:
                        switchToCompletedTasks();
                        break;
                    case R.id.tasksArchivedNavItem:
                        switchToArchivedTasks();
                        break;
                    case R.id.helpNavItem:
                        showHelpDialog();
                        break;
                    case R.id.settingsNavItem:
                        showSettingsFragment();
                        break;
                    case R.id.exitNavItem:
                        exitApplication();
                        break;
                }
                ((MainActivity)getContext()).hideFragment();
                return true;
            }
        });
    }

    private void switchToCurrentTasks() {
        Toast.makeText(getContext(), "switch to current tasks", Toast.LENGTH_SHORT).show();
    }

    private void switchToCompletedTasks() {
        Toast.makeText(getContext(), "switch to completed tasks", Toast.LENGTH_SHORT).show();
    }

    private void switchToArchivedTasks() {
        Toast.makeText(getContext(), "switch to archived tasks", Toast.LENGTH_SHORT).show();
    }

    private void showHelpDialog() {
        Toast.makeText(getContext(), "switch to help dialog", Toast.LENGTH_SHORT).show();
    }

    private void showSettingsFragment() {
        Toast.makeText(getContext(), "switch to settings", Toast.LENGTH_SHORT).show();
    }

    private void exitApplication() {
        getActivity().finish();
    }
}