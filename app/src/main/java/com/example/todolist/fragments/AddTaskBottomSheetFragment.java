package com.example.todolist.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.todolist.R;
import com.example.todolist.activities.MainActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.addTaskTitleEditText)
    TextInputEditText addTaskTitleEditText;
    @BindView(R.id.addTaskTextEditText)
    TextInputEditText addTaskTextEditText;
    @BindView(R.id.addTaskBottomSheetToolbar)
    MaterialToolbar addTaskBottomSheetToolbar;
    @BindView(R.id.rootAddTaskkBottomSheetLayout)
    LinearLayout rootAddTaskkBottomSheetLayout;

    public AddTaskBottomSheetFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottomsheet_add_task, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        initView();
        return view;
    }

    private void initView() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rootAddTaskkBottomSheetLayout.getLayoutParams();
        params.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        rootAddTaskkBottomSheetLayout.setLayoutParams(params);

        addTaskBottomSheetToolbar.inflateMenu(R.menu.add_task_menu);
        addTaskBottomSheetToolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);
        addTaskBottomSheetToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getContext()).killAddTaskBottomSheetFragment();
                addTaskTextEditText.setText("");
                addTaskTitleEditText.setText("");
            }
        });

        addTaskBottomSheetToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (R.id.addTaskMenuItem == item.getItemId()) {
                    String tasksTitle = addTaskTitleEditText.getText().toString();
                    String tasksText = addTaskTextEditText.getText().toString();
                    ((MainActivity) getContext()).addTaskFromFAB(tasksTitle, tasksText);
                    addTaskTextEditText.setText("");
                    addTaskTitleEditText.setText("");
                }
                return true;
            }
        });

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog dialog1 = (BottomSheetDialog) dialog;
                View bottomsheetInternal = dialog1.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomsheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        addTaskTextEditText.setScroller(new Scroller(getContext()));
        addTaskTextEditText.setVerticalScrollBarEnabled(true);
        addTaskTextEditText.setMovementMethod(new ScrollingMovementMethod());

        if (addTaskTitleEditText.requestFocus()) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
            );
        }
    }
}
