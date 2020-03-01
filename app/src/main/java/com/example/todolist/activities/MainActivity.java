package com.example.todolist.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.todolist.R;
import com.example.todolist.fragments.BottomNavigationDrawerFragment;
import com.example.todolist.fragments.CurrentTasksFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.addTaskFAB)
    FloatingActionButton addTaskFAB;

    private BottomNavigationDrawerFragment bottomNavigationDrawerFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        setSupportActionBar(bottomAppBar);
        bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, new CurrentTasksFragment()).commit();
        /*loadTasks();

        //Light mode status bar with black icons
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        todoListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        todoListRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter tasksAdapter = new TasksAdapter(tasks, this);
        todoListRecyclerView.setAdapter(tasksAdapter);

        Functionality when done is pressed on the keyboard for the newTaskEditText
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
    }

    /*private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newTaskEditText.getWindowToken(), 0);
    }

    private void addTask(String taskType) {
        switch (taskType) {
            case "current":
                if (!newTaskEditText.getText().toString().isEmpty()) {
                    tasks.add(newTaskEditText.getText().toString());
                    saveTasks();
                }
                break;
            case "completed":

                break;
            case "archived":

                break;
        }
    }*/

    private void removeTask(String taskType) {

    }

   public void hideFragment() {
       fragmentManager.beginTransaction().remove(bottomNavigationDrawerFragment).commit();
   }

   public void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
   }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId())
            bottomNavigationDrawerFragment.show(fragmentManager, bottomNavigationDrawerFragment.getTag());
        return super.onOptionsItemSelected(item);
    }
}