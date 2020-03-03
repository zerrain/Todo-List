package com.example.todolist;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteArchiveCallback extends ItemTouchHelper.SimpleCallback {

    private TasksAdapter tasksAdapter;
    private Drawable deleteIcon;
    private GradientDrawable deleteBG;
    private Drawable archiveIcon;
    private GradientDrawable archiveBG;
    private String currentFragment;

    public SwipeToDeleteArchiveCallback(TasksAdapter adapter, String fragment) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        tasksAdapter = adapter;
        currentFragment = fragment;
        deleteIcon = ContextCompat.getDrawable(tasksAdapter.getContext(), R.drawable.ic_baseline_delete_24);
        deleteBG = (GradientDrawable) ContextCompat.getDrawable(tasksAdapter.getContext(), R.drawable.delete_swipe_bg);
        archiveIcon = ContextCompat.getDrawable(tasksAdapter.getContext(), R.drawable.ic_baseline_archived_24);
        archiveBG = (GradientDrawable) ContextCompat.getDrawable(tasksAdapter.getContext(), R.drawable.archive_swipe_bg);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int bgCornerOffset = 20;

        int archiveIconMargin = (itemView.getHeight() - archiveIcon.getIntrinsicHeight()) / 2;
        int archiveIconTop = itemView.getTop() + (itemView.getHeight() - archiveIcon.getIntrinsicHeight()) / 2;
        int archiveIconBottom = archiveIconTop + archiveIcon.getIntrinsicHeight();

        int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int deleteIconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int deleteIconBottom = deleteIconTop + deleteIcon.getIntrinsicHeight();

        if (dX < 0) {
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - deleteIcon.getIntrinsicWidth();
            int deleteIconRight = itemView.getRight() - deleteIconMargin;
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);

            deleteBG.setBounds(itemView.getRight() + ((int) dX) - bgCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            deleteBG.draw(c);
            deleteIcon.draw(c);
        } else if (dX > 0) {
            if (currentFragment.equals("current") || currentFragment.equals("archived")) {
                int archiveIconLeft = itemView.getLeft() + archiveIconMargin + archiveIcon.getIntrinsicWidth();
                int archiveIconRight = itemView.getLeft() + archiveIconMargin;
                archiveIcon.setBounds(archiveIconRight, archiveIconTop, archiveIconLeft, archiveIconBottom);

                archiveBG.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + bgCornerOffset, itemView.getBottom());
                archiveBG.draw(c);
                archiveIcon.draw(c);
            }
            else if (currentFragment.equals("completed")) {
                int deleteIconLeft = itemView.getLeft() + deleteIconMargin + deleteIcon.getIntrinsicWidth();
                int deleteIconRight = itemView.getLeft() + deleteIconMargin;
                deleteIcon.setBounds(deleteIconRight, deleteIconTop, deleteIconLeft, deleteIconBottom);

                deleteBG.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + bgCornerOffset, itemView.getBottom());
                deleteBG.draw(c);
                deleteIcon.draw(c);
            }
        } else {
            archiveBG.setBounds(0, 0, 0, 0);
            deleteBG.setBounds(0, 0, 0, 0);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT)
            tasksAdapter.swipeToDeleteItem(position);
        else if (direction == ItemTouchHelper.RIGHT)
            if (currentFragment.equals("current"))
                tasksAdapter.swipeToArchiveItem(position);
            else if (currentFragment.equals("archived"))
                tasksAdapter.swipeToUnarchiveItem(position);
            else if (currentFragment.equals("completed"))
                tasksAdapter.swipeToDeleteItem(position);
    }
}
