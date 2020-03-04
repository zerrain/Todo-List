package com.example.todolist.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.todolist.R;
import com.example.todolist.Task;
import com.example.todolist.fragments.ArchivedTasksFragment;
import com.example.todolist.fragments.BottomNavigationDrawerFragment;
import com.example.todolist.fragments.CompletedTasksFragment;
import com.example.todolist.fragments.CurrentTasksFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private enum NavMenuStates {
        CURRENT_TASKS, COMPLETED_TASKS, ARCHIVED_TASKS
    }

    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.addTaskFAB)
    FloatingActionButton addTaskFAB;
    @BindView(R.id.mainToolbar)
    MaterialToolbar mainToolbar;

    private NavMenuStates currentState = NavMenuStates.CURRENT_TASKS;
    private String currentDate;
    private BottomNavigationDrawerFragment bottomNavigationDrawerFragment;
    private FragmentManager fragmentManager;
    private Fragment fragment;

    File currentTasksFile;
    ArrayList<Task> currentTasks;
    File completedTasksFile;
    ArrayList<Task> completedTasks;
    File archivedTasksFile;
    ArrayList<Task> archivedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String datePattern = "dd/MM/yyyy";
        currentDate = new SimpleDateFormat(datePattern).format(new Date());
        setSupportActionBar(bottomAppBar);
        mainToolbar.setTitle("Current Tasks");
        bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
        currentTasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        archivedTasks = new ArrayList<>();
        currentTasksFile = new File(getFilesDir(), "currentTasks.txt");
        completedTasksFile = new File(getFilesDir(), "completedTasks.txt");
        archivedTasksFile = new File(getFilesDir(), "archivedTasks.txt");
        loadCurrentTasks();
        loadCompletedTasks();
        loadArchivedTasks();
        fragment = new CurrentTasksFragment(currentTasks);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();

        //Light mode status bar with black icons
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @OnClick(R.id.addTaskFAB)
    public void onViewClicked() {
        LinearLayout linearLayout = new LinearLayout(getBaseContext());
        final EditText taskTitleEditText = new EditText(this);
        final EditText taskTextEditText = new EditText(this);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        taskTitleEditText.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        taskTextEditText.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(taskTitleEditText);
        linearLayout.addView(taskTextEditText);

        AlertDialog addTaskDialog = new AlertDialog.Builder(this).create();
        addTaskDialog.setTitle("New Task");
        addTaskDialog.setMessage("Input new task in box");
        addTaskDialog.setView(linearLayout);
        addTaskDialog.getWindow().setBackgroundDrawableResource(R.drawable.help_dialog_bg);

        addTaskDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> {
                    if (taskTitleEditText.getText().toString().isEmpty() && taskTextEditText.getText().toString().isEmpty())
                        Toast.makeText(MainActivity.this, "No Task Entered!", Toast.LENGTH_SHORT).show();
                    else {
                        String taskTitle = taskTitleEditText.getText().toString();
                        String taskText = taskTextEditText.getText().toString();
                        if (taskTitle.isEmpty())
                            taskTitle = " ";
                        if (taskText.isEmpty())
                            taskText = " ";

                        Task task = new Task("Added: " + currentDate,
                                "Last Edited: " + currentDate,
                                taskTitle, taskText);
                        addCurrentTask(task);
                        Toast.makeText(getBaseContext(), task.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        addTaskDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                (dialog, which) -> dialog.dismiss());

        addTaskDialog.show();
        addTaskDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        addTaskDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
    }

    public void hideFAB() {
        addTaskFAB.hide();
    }

    public void showFAB() {
        addTaskFAB.show();
    }

    public void addCurrentTask(Task newCurrentTask) {
        currentTasks.add(newCurrentTask);
        saveCurrentTasks(currentTasks);
    }

    public void updateTasks(ArrayList<Task> tasks) {
        if (currentState == NavMenuStates.CURRENT_TASKS)
            saveCurrentTasks(tasks);
        else if (currentState == NavMenuStates.COMPLETED_TASKS)
            saveCompletedTasks(tasks);
        else if (currentState == NavMenuStates.ARCHIVED_TASKS)
            saveArchivedTasks(tasks);
    }

    public void revertToCurrentTask(ArrayList<Task> tasks, Task newCurrentTask) {
        if (currentState == NavMenuStates.COMPLETED_TASKS)
            saveCompletedTasks(tasks);
        else if (currentState == NavMenuStates.ARCHIVED_TASKS)
            saveArchivedTasks(tasks);
        currentTasks.add(newCurrentTask);
        saveCurrentTasks(currentTasks);
    }

    public void addCompletedTask(ArrayList<Task> tasks, Task completedTask) {
        if (currentState == NavMenuStates.CURRENT_TASKS)
            saveCurrentTasks(tasks);
        else if (currentState == NavMenuStates.ARCHIVED_TASKS)
            saveArchivedTasks(tasks);
        completedTasks.add(completedTask);
        saveCompletedTasks(completedTasks);
    }

    public void addArchivedTask(ArrayList<Task> tasks, Task archivedTask) {
        if (currentState == NavMenuStates.CURRENT_TASKS)
            saveCurrentTasks(tasks);
        else if (currentState == NavMenuStates.COMPLETED_TASKS)
            saveCompletedTasks(tasks);
        archivedTasks.add(archivedTask);
        saveArchivedTasks(archivedTasks);
    }

    public void hideFragment() {
        fragmentManager.beginTransaction().remove(bottomNavigationDrawerFragment).commit();
    }

    public void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    public void switchToCurrentTasks() {
            currentState = NavMenuStates.CURRENT_TASKS;
            loadCurrentTasks();
            fragment = new CurrentTasksFragment(currentTasks);
            replaceFragment(fragment);
            mainToolbar.setTitle("Current Tasks");
            showFAB();
    }

    public void switchToCompletedTasks() {
            currentState = NavMenuStates.COMPLETED_TASKS;
            loadCompletedTasks();
            fragment = new CompletedTasksFragment(completedTasks);
            replaceFragment(fragment);
            mainToolbar.setTitle("Completed Tasks");
            showFAB();
    }

    public void switchToArchivedTasks() {
            currentState = NavMenuStates.ARCHIVED_TASKS;
            loadArchivedTasks();
            fragment = new ArchivedTasksFragment(archivedTasks);
            replaceFragment(fragment);
            mainToolbar.setTitle("Archived Tasks");
            showFAB();
    }

    public void showSettingsFragment() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    public void showHelpDialog() {
        AlertDialog helpDialog = new AlertDialog.Builder(this).create();
        helpDialog.setTitle("Help");
        helpDialog.setMessage("Swipe right to archive, left to delete");
        helpDialog.getWindow().setBackgroundDrawableResource(R.drawable.help_dialog_bg);
        helpDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> dialog.dismiss());

        helpDialog.show();
        helpDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        helpDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
    }

    public void saveCurrentTasks(ArrayList<Task> newCurrentTasks) {
        try {
            FileUtils.writeLines(currentTasksFile, newCurrentTasks);
            currentTasks = newCurrentTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCurrentTasks() {
        try {
            currentTasks = new ArrayList<>();
            for (String line : FileUtils.readLines(currentTasksFile)) {
                String[] parts = line.split("`");
                currentTasks.add(new Task(parts[0], parts[1], parts[2], parts[3]));
            }
            //currentTasks = new ArrayList<>(FileUtils.readLines(currentTasksFile));
        } catch (IOException e) {
            e.printStackTrace();
            currentTasks = new ArrayList<>();
            try {
                currentTasksFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveCompletedTasks(ArrayList<Task> newCompletedTasks) {
        try {
            FileUtils.writeLines(completedTasksFile, newCompletedTasks);
            completedTasks = newCompletedTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCompletedTasks() {
        try {
            completedTasks = new ArrayList<>();
            for (String line : FileUtils.readLines(completedTasksFile)) {
                String[] parts = line.split("`");
                completedTasks.add(new Task(parts[0], parts[1], parts[2], parts[3]));
            }
            //completedTasks = new ArrayList<>(FileUtils.readLines(completedTasksFile));
        } catch (IOException e) {
            e.printStackTrace();
            completedTasks = new ArrayList<>();
            try {
                completedTasksFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveArchivedTasks(ArrayList<Task> newArchivedTasks) {
        try {
            FileUtils.writeLines(archivedTasksFile, newArchivedTasks);
            archivedTasks = newArchivedTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadArchivedTasks() {
        try {
            archivedTasks = new ArrayList<>();
            for (String line : FileUtils.readLines(archivedTasksFile)) {
                String[] parts = line.split("`");
                archivedTasks.add(new Task(parts[0], parts[1], parts[2], parts[3]));
            }
            //archivedTasks = new ArrayList<>(FileUtils.readLines(archivedTasksFile));
        } catch (Exception e) {
            e.printStackTrace();
            archivedTasks = new ArrayList<>();
            try {
                archivedTasksFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getCurrentState() {
        return currentState.toString();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId())
            bottomNavigationDrawerFragment.show(fragmentManager, bottomNavigationDrawerFragment.getTag());
        return super.onOptionsItemSelected(item);
    }
}