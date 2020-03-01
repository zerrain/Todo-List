package com.example.todolist.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.R;
import com.example.todolist.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    private enum NavMenuStates {
        CURRENT_TASKS, COMPLETED_TASKS, ARCHIVED_TASKS, SETTINGS
    }

    private NavMenuStates currentState = NavMenuStates.CURRENT_TASKS;

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
        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        NavigationView bottomNavView = view.findViewById(R.id.bottomNavView);
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
                    case R.id.settingsNavItem:
                        showSettingsFragment();
                        break;
                    case R.id.helpNavItem:
                        showHelpDialog();
                        break;
                    case R.id.exitNavItem:
                        getActivity().finish();
                        break;
                }
                ((MainActivity) getContext()).hideFragment();
                return true;
            }
        });
    }

    private void switchToCurrentTasks() {
        if (currentState != NavMenuStates.CURRENT_TASKS) {
            currentState = NavMenuStates.CURRENT_TASKS;
            ((MainActivity)getActivity()).replaceFragment(new CurrentTasksFragment());
        }
    }

    private void switchToCompletedTasks() {
        if (currentState != NavMenuStates.COMPLETED_TASKS) {
            currentState = NavMenuStates.COMPLETED_TASKS;
            ((MainActivity)getActivity()).replaceFragment(new CompletedTasksFragment());
        }
    }

    private void switchToArchivedTasks() {
        if (currentState != NavMenuStates.ARCHIVED_TASKS) {
            currentState = NavMenuStates.ARCHIVED_TASKS;
            ((MainActivity)getActivity()).replaceFragment(new ArchivedTasksFragment());
        }
    }

    private void showSettingsFragment() {
        if (currentState != NavMenuStates.SETTINGS) {
            currentState = NavMenuStates.SETTINGS;
            ((MainActivity)getActivity()).replaceFragment(new SettingsFragment());
        }
    }

    private void showHelpDialog() {
        AlertDialog helpDialog = new AlertDialog.Builder(getContext()).create();
        helpDialog.setTitle("Help");
        helpDialog.setMessage("Swipe right to archive, left to delete");
        helpDialog.getWindow().setBackgroundDrawableResource(R.drawable.help_dialog_bg);
        helpDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        helpDialog.show();
        helpDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        helpDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
    }
}