package com.example.todolist;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    private ImageView tasksCompletedImageView;
    private ImageView tasksUndoCompletedImageView;
    private ArrayList<Task> tasks;
    private Context context;
    private Snackbar undoSnackbar;
    public Task lastDeletedTask;
    public int lastDeletedTaskPosition;

    public static class TasksViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView textViewTitle;
        public TextView textViewText;
        public TextView tasksDateAddedTextView;
        public TextView tasksDateEditedTextView;

        public TasksViewHolder(CardView v) {
            super(v);
            cardView = v;
            textViewTitle = v.findViewById(R.id.tasksTitleTextView);
            textViewText = v.findViewById(R.id.tasksTextView);
            tasksDateAddedTextView = v.findViewById(R.id.tasksDateAddedTextView);
            tasksDateEditedTextView = v.findViewById(R.id.tasksDateEditedTextView);
        }
    }

    public TasksAdapter(ArrayList<Task> tasks, Context context) {
        this.tasks = new ArrayList<>();
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_cardview, parent, false);

        tasksCompletedImageView = v.findViewById(R.id.tasksCompletedImageView);
        tasksUndoCompletedImageView = v.findViewById(R.id.tasksUndoCompletedImageView);

        return new TasksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TasksViewHolder holder, final int position) {
        initHolderViews(holder);
    }

    private void initHolderViews(TasksViewHolder holder) {
        int taskPosition = holder.getLayoutPosition();
        holder.textViewTitle.setText(tasks.get(taskPosition).getTaskTitle());
        holder.textViewText.setText(tasks.get(taskPosition).getTaskText());
        holder.tasksDateAddedTextView.setText(tasks.get(taskPosition).getDateAdded());
        holder.tasksDateEditedTextView.setText(tasks.get(taskPosition).getDateLastEdited());

        if (((MainActivity) context).getCurrentState().equals("COMPLETED_TASKS")) {
            tasksCompletedImageView.setVisibility(View.GONE);
            tasksUndoCompletedImageView.setVisibility(View.VISIBLE);
            tasksUndoCompletedImageView.setOnClickListener(v -> undoCompleteItem(holder));

            if (holder.textViewTitle.getText().toString().equals(" ")) {
                holder.textViewTitle.setVisibility(View.GONE);
                holder.textViewText.setPaintFlags(holder.textViewText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else if (holder.textViewText.getText().toString().equals(" ")) {
                holder.textViewText.setVisibility(View.GONE);
                holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.textViewText.setPaintFlags(holder.textViewText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        } else {
            tasksUndoCompletedImageView.setVisibility(View.GONE);
            tasksCompletedImageView.setVisibility(View.VISIBLE);
            tasksCompletedImageView.setOnClickListener(v -> completeItem(holder));

            if (holder.textViewTitle.getText().toString().equals(" "))
                holder.textViewTitle.setVisibility(View.GONE);
            else if (holder.textViewText.getText().toString().equals(" "))
                holder.textViewText.setVisibility(View.GONE);
        }

        undoSnackbar = Snackbar.make(((MainActivity) context).findViewById(R.id.fragmentContainer),
                "Task Deleted!", Snackbar.LENGTH_SHORT);
        undoSnackbar.setAnchorView(((MainActivity) context).findViewById(R.id.addTaskFAB));
        undoSnackbar.setAction("Undo", v -> {
            tasks.add(lastDeletedTaskPosition, lastDeletedTask);
            notifyItemInserted(lastDeletedTaskPosition);
            ((MainActivity) context).updateTasks(tasks);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (((MainActivity) context).getCurrentState().toString()) {
                        case "CURRENT_TASKS":
                            ((MainActivity) context).switchToCurrentTasks();
                            break;
                        case "ARCHIVED_TASKS":
                            ((MainActivity) context).switchToArchivedTasks();
                            break;
                        case "COMPLETED_TASKS":
                            ((MainActivity) context).switchToCompletedTasks();
                            break;
                    }
                }
            }, 300);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void swipeToDeleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            lastDeletedTask = tasks.get(position);
            lastDeletedTaskPosition = position;
            tasks.remove(position);
            ((MainActivity) context).updateTasks(tasks);
            notifyItemRemoved(position);
            undoSnackbar.show();
        }
    }

    public void swipeToArchiveItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Task archivedTask = tasks.get(position);
            tasks.remove(position);
            ((MainActivity) context).addArchivedTask(tasks, archivedTask);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task Archived!", Toast.LENGTH_SHORT).show();
        }
    }

    public void swipeToUnarchiveItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Task archivedTask = tasks.get(position);
            tasks.remove(position);
            ((MainActivity) context).revertToCurrentTask(tasks, archivedTask);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task Unarchived!", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeItem(TasksViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (position == RecyclerView.NO_POSITION) {
            Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();
        }
        else {
            Task completedTask = tasks.get(position);
        tasks.remove(position);
        ((MainActivity) context).addCompletedTask(tasks, completedTask);
        notifyItemRemoved(position);
        Toast.makeText(context, "Task Completed!", Toast.LENGTH_SHORT).show();
        }

    }

    private void undoCompleteItem(TasksViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (position != RecyclerView.NO_POSITION) {
            Task completedTask = tasks.get(position);
            tasks.remove(position);
            ((MainActivity) context).addCurrentTask(completedTask);
            ((MainActivity) context).updateTasks(tasks);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task Completion Undone!", Toast.LENGTH_SHORT).show();
        }
    }

    public Context getContext() {
        return context;
    }
}
