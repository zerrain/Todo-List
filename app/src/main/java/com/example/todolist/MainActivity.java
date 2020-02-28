package com.example.todolist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    MaterialToolbar mainToolbar;
    @BindView(R.id.todoListRecyclerView)
    RecyclerView todoListRecyclerView;
    @BindView(R.id.newTaskEditText)
    TextInputEditText newTaskEditText;
    @BindView(R.id.newTaskEditTextLayout)
    TextInputLayout newTaskEditTextLayout;

    ArrayList<String> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mainToolbar);

        todoListRecyclerView.setHasFixedSize(true);

        tasks = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        todoListRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter tasksAdapter = new TasksAdapter(tasks);
        todoListRecyclerView.setAdapter(tasksAdapter);

        //Enables the newTaskEditText cursor when it is tapped on
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
                    newTaskEditText.setCursorVisible(false);
                    newTaskEditText.setText(null);
                    newTaskEditTextLayout.setVisibility(View.GONE);
                    newTaskEditTextLayout.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        //Light mode status bar with black icons
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newTaskEditText.getWindowToken(), 0);
    }

    private void addTask() {
        if (!newTaskEditText.getText().toString().isEmpty())
            tasks.add(newTaskEditText.getText().toString());
        else
            Toast.makeText(this, "No task entered", Toast.LENGTH_SHORT).show();
    }
}