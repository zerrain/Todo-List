package com.example.todolist;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.todoListRecyclerView)
    RecyclerView todoListRecyclerView;
    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.addTaskFAB)
    FloatingActionButton addTaskFAB;

    ArrayList<String> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        todoListRecyclerView.setHasFixedSize(true);
        setSupportActionBar(bottomAppBar);

        loadTasks();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        todoListRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter tasksAdapter = new TasksAdapter(tasks, this);
        todoListRecyclerView.setAdapter(tasksAdapter);

        /*//Enables the newTaskEditText cursor when it is tapped on
        newTaskEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == newTaskEditText.getId())
                    newTaskEditText.setCursorVisible(true);
            }
        });

        //Functionality when done is pressed on the keyboard for the newTaskEditText
        newTaskEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addTask();
                    hideKeyboard();
                    resetEditTextStyle();
                    return true;
                }
                return false;
            }
        });*/

        //Light mode status bar with black icons
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /*private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newTaskEditText.getWindowToken(), 0);
    }

    private void addTask() {
        if (!newTaskEditText.getText().toString().isEmpty()) {
            tasks.add(newTaskEditText.getText().toString());
            saveTasks();
        }
        else
            Toast.makeText(this, "No task entered", Toast.LENGTH_SHORT).show();
    }*/

    public void loadTasks() {
        File filesDir = getFilesDir();
        File tasksFile = new File(filesDir, "tasks.txt");
        try {
            tasks = new ArrayList<String>(FileUtils.readLines(tasksFile));
        } catch (Exception e) {
            e.printStackTrace();
            tasks = new ArrayList<>();
        }
    }

    public void saveTasks() {
        File filesDir = getFilesDir();
        File tasksFile = new File(filesDir, "tasks.txt");
        try {
            FileUtils.writeLines(tasksFile, tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private void resetEditTextStyle() {
        newTaskEditText.setCursorVisible(false);
        newTaskEditText.setText(null);
        newTaskEditTextLayout.setVisibility(View.GONE);
        newTaskEditTextLayout.setVisibility(View.VISIBLE);
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            BottomNavigationDrawerFragment bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
            bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.getTag());
        }
        return super.onOptionsItemSelected(item);
    }
}