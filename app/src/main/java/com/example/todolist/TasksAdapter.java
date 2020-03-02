package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.activities.MainActivity;
import com.example.todolist.fragments.CurrentTasksFragment;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    AppCompatImageView taskCompleteCheckIcon;
    AppCompatImageView taskDeleteBinicon;
    private ArrayList<String> tasks;
    private Context context;

    public static class TasksViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView textView;

        public TasksViewHolder(CardView v) {
            super(v);
            cardView = v;
            textView = v.findViewById(R.id.tasksTextView);
        }
    }

    public TasksAdapter(ArrayList<String> tasks, Context context) {
        this.tasks = new ArrayList<>();
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_cardview, parent, false);

        taskDeleteBinicon = v.findViewById(R.id.taskDeleteBinicon);
        taskCompleteCheckIcon = v.findViewById(R.id.taskCompleteCheckIcon);

        return new TasksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TasksViewHolder holder, final int position) {
        holder.textView.setText(tasks.get(position));

        taskDeleteBinicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(holder, v);
            }
        });

        taskCompleteCheckIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeItem(holder, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private void deleteItem(TasksViewHolder holder, View v) {
        if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
            tasks.remove(holder.getAdapterPosition());
            ((MainActivity) context).deleteTask(tasks);
            notifyItemRemoved(holder.getAdapterPosition());
            Toast.makeText(v.getContext(), "del pressed", Toast.LENGTH_SHORT).show();
        }
    }

    public void swipeToDeleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            tasks.remove(position);
            ((MainActivity) context).deleteTask(tasks);
            notifyItemRemoved(position);
            Toast.makeText(context, "del swiped", Toast.LENGTH_SHORT).show();
        }
    }

    public void swipeToArchiveItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            String archivedTask = tasks.get(position);
            tasks.remove(position);
            ((MainActivity) context).addArchivedTask(tasks, archivedTask);
            notifyItemRemoved(position);
            Toast.makeText(context, "arch swiped", Toast.LENGTH_SHORT).show();
        }
    }

    public void swipeToUnarchiveItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            String archivedTask = tasks.get(position);
            tasks.remove(position);
            ((MainActivity) context).revertToCurrentTask(tasks, archivedTask);
            notifyItemRemoved(position);
            Toast.makeText(context, "unarch swiped", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeItem(TasksViewHolder holder, View v) {
        if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
            String completedTask = tasks.get(holder.getAdapterPosition());
            tasks.remove(holder.getAdapterPosition());
            ((MainActivity) context).addCompletedTask(tasks, completedTask);
            notifyItemRemoved(holder.getAdapterPosition());
            Toast.makeText(v.getContext(), "complete pressed", Toast.LENGTH_SHORT).show();
        }
    }

    public Context getContext() {
        return context;
    }
}
