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
                        ((MainActivity)getActivity()).switchToCurrentTasks();
                        break;
                    case R.id.tasksCompletedNavItem:
                        ((MainActivity)getActivity()).switchToCompletedTasks();
                        break;
                    case R.id.tasksArchivedNavItem:
                        ((MainActivity)getActivity()).switchToArchivedTasks();
                        break;
                    case R.id.settingsNavItem:
                        ((MainActivity)getActivity()).showSettingsFragment();
                        break;
                    case R.id.helpNavItem:
                        ((MainActivity)getActivity()).showHelpDialog();
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
}