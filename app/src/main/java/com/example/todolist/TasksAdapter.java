package com.example.todolist;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    private ArrayList<String> tasks;

    public static class TasksViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public TasksViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    public TasksAdapter(ArrayList<String> tasks) {
        this.tasks = new ArrayList<>();
        this.tasks = tasks;
    }

    @NonNull
    public TasksAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_textview, parent, false);
        return new TasksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        holder.textView.setText(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
