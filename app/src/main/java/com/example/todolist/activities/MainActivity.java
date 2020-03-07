package com.example.todolist.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.todolist.R;
import com.example.todolist.Task;
import com.example.todolist.fragments.AddTaskBottomSheetFragment;
import com.example.todolist.fragments.ArchivedTasksFragment;
import com.example.todolist.fragments.BottomNavigationDrawerFragment;
import com.example.todolist.fragments.CompletedTasksFragment;
import com.example.todolist.fragments.CurrentTasksFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private AddTaskBottomSheetFragment addTaskBottomSheetFragment;
    private FragmentManager fragmentManager;
    private Fragment fragment;

    private File currentTasksFile;
    private ArrayList<Task> currentTasks;
    private File completedTasksFile;
    private ArrayList<Task> completedTasks;
    private File archivedTasksFile;
    private ArrayList<Task> archivedTasks;

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
        addTaskBottomSheetFragment = new AddTaskBottomSheetFragment();
        currentTasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        archivedTasks = new ArrayList<>();
        currentTasksFile = new File(getFilesDir(), "currentTasks");
        completedTasksFile = new File(getFilesDir(), "completedTasks");
        archivedTasksFile = new File(getFilesDir(), "archivedTasks");
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
        if (!addTaskBottomSheetFragment.isAdded())
            addTaskBottomSheetFragment.show(fragmentManager, addTaskBottomSheetFragment.getTag());
    }

    public ArrayList<Task> getCompletedTasks() {
        return completedTasks;
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

    public void addTaskFromFAB(String enteredTaskTitle, String enteredTaskText) {
        if (enteredTaskText.isEmpty() && enteredTaskTitle.isEmpty())
            Toast.makeText(MainActivity.this, "No Task Entered!", Toast.LENGTH_SHORT).show();
        else {
            String taskTitle = enteredTaskTitle;
            String taskText = enteredTaskText;
            if (taskTitle.isEmpty())
                taskTitle = " ";
            if (taskText.isEmpty())
                taskText = " ";

            Task task = new Task("Added: " + currentDate,
                    "Last Edited: " + currentDate,
                    taskTitle, taskText);
            addCurrentTask(task);
            if (currentState.toString().equals("CURRENT_TASKS"))
                switchToCurrentTasks();
            Toast.makeText(getBaseContext(), task.toString(), Toast.LENGTH_SHORT).show();
            killAddTaskBottomSheetFragment();
        }
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

    public void hideNavBottomSheetFragment() {
        fragmentManager.beginTransaction().remove(bottomNavigationDrawerFragment).commit();
    }

    public void killAddTaskBottomSheetFragment() {
        fragmentManager.beginTransaction().remove(addTaskBottomSheetFragment).commitAllowingStateLoss();
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
            FileOutputStream fos = new FileOutputStream(currentTasksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(newCurrentTasks);
            oos.close();
            currentTasks = newCurrentTasks;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCurrentTasks() {
        try {
            FileInputStream fis = new FileInputStream(currentTasksFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            currentTasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            currentTasks = new ArrayList<>();
            try {
                currentTasksFile.createNewFile();
                System.out.println("Current task file created");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveCompletedTasks(ArrayList<Task> newCompletedTasks) {
        try {
            FileOutputStream fos = new FileOutputStream(completedTasksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(newCompletedTasks);
            oos.close();
            completedTasks = newCompletedTasks;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCompletedTasks() {
        try {
            FileInputStream fis = new FileInputStream(completedTasksFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            completedTasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            completedTasks = new ArrayList<>();
            try {
                completedTasksFile.createNewFile();
                System.out.println("Completed task file created");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveArchivedTasks(ArrayList<Task> newArchivedTasks) {
        try {
            FileOutputStream fos = new FileOutputStream(archivedTasksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(newArchivedTasks);
            oos.close();
            archivedTasks = newArchivedTasks;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadArchivedTasks() {
        try {
            FileInputStream fis = new FileInputStream(archivedTasksFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            archivedTasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            archivedTasks = new ArrayList<>();
            try {
                archivedTasksFile.createNewFile();
                System.out.println("Archived task file created");
            } catch (Exception ex) {
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
            if (!bottomNavigationDrawerFragment.isAdded())
                bottomNavigationDrawerFragment.show(fragmentManager, bottomNavigationDrawerFragment.getTag());
        return super.onOptionsItemSelected(item);
    }
}