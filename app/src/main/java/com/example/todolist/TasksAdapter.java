package com.example.todolist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.activities.MainActivity;
import com.google.android.material.checkbox.MaterialCheckBox;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    private ImageView tasksCompletedImageView;
    private ImageView tasksUndoCompletedImageView;
    private ArrayList<String> tasks;
    private Context context;

    public static class TasksViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView textViewTitle;
        public TextView textViewText;

        public TasksViewHolder(CardView v) {
            super(v);
            cardView = v;
            textViewTitle = v.findViewById(R.id.tasksTitleTextView);
            textViewText = v.findViewById(R.id.tasksTextView);
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

        tasksCompletedImageView = v.findViewById(R.id.tasksCompletedImageView);
        tasksUndoCompletedImageView = v.findViewById(R.id.tasksUndoCompletedImageView);

        return new TasksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TasksViewHolder holder, final int position) {
        holder.textViewTitle.setText(tasks.get(position));

        if (((MainActivity) context).getCurrentState().equals("COMPLETED_TASKS")) {
            tasksCompletedImageView.setVisibility(View.GONE);
            tasksUndoCompletedImageView.setVisibility(View.VISIBLE);
            tasksUndoCompletedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoCompleteItem(holder);
                }
            });

            holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textViewText.setPaintFlags(holder.textViewText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tasksUndoCompletedImageView.setVisibility(View.GONE);
            tasksCompletedImageView.setVisibility(View.VISIBLE);
            tasksCompletedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeItem(holder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void swipeToDeleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            tasks.remove(position);
            ((MainActivity) context).deleteTask(tasks);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void swipeToArchiveItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            String archivedTask = tasks.get(position);
            tasks.remove(position);
            ((MainActivity) context).addArchivedTask(tasks, archivedTask);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task Archived!", Toast.LENGTH_SHORT).show();
        }
    }

    public void swipeToUnarchiveItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            String archivedTask = tasks.get(position);
            tasks.remove(position);
            ((MainActivity) context).revertToCurrentTask(tasks, archivedTask);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task Unarchived!", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeItem(TasksViewHolder holder) {
        if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
            String completedTask = tasks.get(holder.getAdapterPosition());
            tasks.remove(holder.getAdapterPosition());
            ((MainActivity) context).addCompletedTask(tasks, completedTask);
            notifyItemRemoved(holder.getAdapterPosition());
            Toast.makeText(context, "Task Completed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void undoCompleteItem(TasksViewHolder holder) {
        if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
            String completedTask = tasks.get(holder.getAdapterPosition());
            tasks.remove(holder.getAdapterPosition());
            ((MainActivity) context).addCurrentTask(completedTask);
            ((MainActivity) context).deleteTask(tasks);
            notifyItemRemoved(holder.getAdapterPosition());
            Toast.makeText(context, "Task Completion Undone!", Toast.LENGTH_SHORT).show();
        }
    }

    public Context getContext() {
        return context;
    }
}
